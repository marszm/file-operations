package com.example.fo.controller;

import com.example.fo.dto.FileUploadResponse;
import com.example.fo.model.FileDocument;
import com.example.fo.service.DocFileDao;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;

@RestController
@AllArgsConstructor
public class UploadDownloadDBController {

    private final DocFileDao docFileDao;

    @PostMapping("single/uploadToDb")
    public FileUploadResponse singleFileUpload(@RequestParam("multipartFile") MultipartFile multipartFile) throws IOException {

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileDocument fileDocument = new FileDocument();
        fileDocument.setFileName(fileName);
        fileDocument.setDocFile(multipartFile.getBytes());
        docFileDao.save(fileDocument);

        String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFromDb/")
                .path(fileName)
                .toUriString();

        String contentType = multipartFile.getContentType();

        return new FileUploadResponse(fileName, contentType, url);
    }

    @GetMapping("/downloadFromDb/{fileName}")
    public ResponseEntity<byte[]> downloadSingleFile(@PathVariable String fileName, HttpServletRequest httpServletRequest) {

        FileDocument fileDocument = docFileDao.findByFileName(fileName);

        String mimeType = httpServletRequest.getServletContext().getMimeType(fileDocument.getFileName());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;fileName=" + fileDocument.getFileName())
                .body(fileDocument.getDocFile());
    }

}
