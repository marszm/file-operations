package com.example.fo.controller;

import com.example.fo.dto.FileUploadResponse;
import com.example.fo.service.FilerStorageService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@AllArgsConstructor
public class UploadDownloadFileSystemController {

    private final FilerStorageService filerStorageService;

    @PostMapping("single/upload")
    public FileUploadResponse singleFileUpload(@RequestParam("file") MultipartFile multipartFile) {
        String fileName = filerStorageService.storeFile(multipartFile);
        String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download")
                .path(fileName)
                .toUriString();
        String contentType = multipartFile.getContentType();
        return new FileUploadResponse(fileName, contentType, url);
    }

}
