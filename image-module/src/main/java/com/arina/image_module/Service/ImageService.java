package com.arina.image_module.Service;

import com.arina.image_module.Dto.ImageInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;


public interface ImageService {
    Object uploadimage(MultipartFile file) throws Exception;
    InputStream viewimage(String filename) throws Exception;
    List<ImageInfo> listimages() throws Exception;
    String deleteimage(String filename) throws Exception;
}
