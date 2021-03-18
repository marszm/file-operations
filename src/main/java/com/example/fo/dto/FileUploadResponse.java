package com.example.fo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FileUploadResponse {

    private String fileName;
    private String contentType;
    private String url;

}
