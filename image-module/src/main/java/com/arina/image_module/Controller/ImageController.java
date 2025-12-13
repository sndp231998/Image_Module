package com.arina.image_module.Controller;

import com.arina.image_module.Service.ImageService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    //upload file
    @PostMapping("/upload")
    public Object upload(@RequestParam MultipartFile file) throws Exception{
        return imageService.uploadimage(file);
    }

//to view image
    @GetMapping("/{fileName}")
    public ResponseEntity<byte []> view(@PathVariable String fileName)throws Exception{
    var stream=imageService.viewimage(fileName);
    byte[] bytes= stream.readAllBytes();
    //to valida the image format ,to preview
        String contentType = fileName.toLowerCase().endsWith(".png")
                ? MediaType.IMAGE_PNG_VALUE
                : MediaType.IMAGE_JPEG_VALUE;
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + fileName)
                .contentType(MediaType.parseMediaType(contentType))
                .body(bytes);
    }

    //To delete Image
    @DeleteMapping("/{fileName}")
    public String delete(@PathVariable String fileName)throws Exception{
        return imageService.deleteimage(fileName);
    }

    //to get list of images
    @GetMapping("/list")
    public Object list()throws Exception{
        return imageService.listimages();
    }



}
