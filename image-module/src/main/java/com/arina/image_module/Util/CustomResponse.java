package com.arina.image_module.Util;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CustomResponse {
    private String message;
    private Object data;
}
