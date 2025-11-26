package com.arina.image_module.ServiceImplementation;

import com.arina.image_module.Dto.ImageInfo;
import com.arina.image_module.Dto.UploadResponse;
import com.arina.image_module.Service.ImageService;
import io.minio.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImageServiceImp implements ImageService{

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucketName;

    @Value("${minio.url}")
    private String minioUrl;

    public ImageServiceImp(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Override
    public Object uploadimage(MultipartFile file) throws Exception {
        if(file.isEmpty()){
            throw new RuntimeException("File is Empty");
        }
        String fileName=System.currentTimeMillis()+"_"+file.getOriginalFilename();
        String contentType=file.getContentType();

        if(!contentType.equals("image/jpg") &&
                !contentType.equals("image/jpeg") &&
                !contentType.equals("image/png"))
        {
            throw new RuntimeException("Only JPEG, PNG, JPG images are allowed");
        }

        //create bucket if missing
        boolean exists= minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());

        if(!exists){
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }

        //upload file
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(contentType)
                        .build()

        );
        String fileUrl = minioUrl + "/" + bucketName + "/" + fileName;

        return new UploadResponse(
                fileName,
                fileUrl,
                file.getSize(),
                bucketName,
                LocalDateTime.now()

        );
    }

    @Override
    public InputStream viewimage(String fileName) throws Exception {

        return minioClient.getObject(
                        GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .build()
        );
    }

    @Override
    public List<ImageInfo> listimages() throws Exception {
        List<ImageInfo> list=new ArrayList<>();

        Iterable<Result<Item>> results=minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucketName)
                        .build()
        );

        for(Result<Item> result:results){
            Item item=result.get();
            list.add(
                    new ImageInfo(
                            item.objectName(),
                            item.size(),
                            item.lastModified().toLocalDateTime()
                    )
            );

        }
        return list;
    }

    @Override
    public String  deleteimage(String fileName) throws Exception {
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .build()
        );
        return "Image deleted Successfully.";

    }

    // -------- Helper DTOs ----------
    record UploadResponse(String fileName, String url, long size,
                          String bucket, LocalDateTime uploadedAt) {}

    record ImageDetailedInfo(String name, long size, LocalDateTime lastModified) {}
}
