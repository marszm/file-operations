package com.example.fo.controller;

import com.example.fo.dto.FileUploadResponse;
import com.example.fo.service.FilerStorageService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@AllArgsConstructor
public class UploadDownloadFileSystemController {

    private final FilerStorageService filerStorageService;

    @PostMapping("single/upload")
    public FileUploadResponse singleFileUpload(@RequestParam("multipartFile") MultipartFile multipartFile) {

        String fileName = filerStorageService.storeFile(multipartFile);

        String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(fileName)
                .toUriString();

        String contentType = multipartFile.getContentType();

        return new FileUploadResponse(fileName, contentType, url);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadSingleFile(@PathVariable String fileName, HttpServletRequest httpServletRequest) {

        Resource resource = filerStorageService.downloadFile(fileName);

        String mimeType;

        try {
            mimeType = httpServletRequest.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;fileName=" + resource.getFilename())
                .body(resource);
    }

    @PostMapping("/multiple/upload")
    List<FileUploadResponse> multipleFileUpload(@RequestParam("multipartFiles") MultipartFile[] multipartFiles) {

        List<FileUploadResponse> fileUploadResponseList = new ArrayList<>();

        Arrays.stream(multipartFiles)
                .forEach(file -> {
                    String fileName = filerStorageService.storeFile(file);
                    String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path("/download/")
                            .path(fileName)
                            .toUriString();
                    String contentType = file.getContentType();
                    FileUploadResponse fileUploadResponse = new FileUploadResponse(fileName, contentType, url);
                    fileUploadResponseList.add(fileUploadResponse);
                });
        return fileUploadResponseList;
    }


}
