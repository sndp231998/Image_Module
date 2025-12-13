package com.arina.image_module.Dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadResponse {
    private String fileName;
    private String url;
    private long size;
    private String bucket;
    private LocalDateTime uploadedAt;
}
