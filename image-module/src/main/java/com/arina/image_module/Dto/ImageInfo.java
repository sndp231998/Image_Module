package com.arina.image_module.Dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageInfo {
    private String name;
    private long size;
    private LocalDateTime lastModified;

}
