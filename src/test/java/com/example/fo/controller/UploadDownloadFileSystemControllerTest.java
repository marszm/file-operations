package com.example.fo.controller;

import com.example.fo.service.FilerStorageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UploadDownloadFileSystemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FilerStorageService filerStorageService;

    @Test
    @DisplayName("test to upload single file")
    void singleFileUpload() throws Exception {

        Mockito.when(filerStorageService.storeFile(any(MultipartFile.class))).thenReturn("test-file.txt");

        MockMultipartFile multipartFile = new MockMultipartFile("multipartFile", "test-file.txt", "text/plain", "aaaa".getBytes());

        this.mockMvc.perform(MockMvcRequestBuilders.multipart("/single/upload").file(multipartFile))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"fileName\":test-file.txt,\"contentType\":\"text/plain\",\"url\":\"http://localhost/download/test-file.txt\"}"));

        BDDMockito.then(this.filerStorageService).should().storeFile(multipartFile);

    }

    @Test
    @DisplayName("single file download test")
    void downloadSingleFile() throws Exception {

        Resource resource = getMockedResource();
        Mockito.when(filerStorageService.downloadFile(anyString())).thenReturn(resource);
        mockMvc.perform(get("/download/abc.jpg"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "inline;fileName=" + resource.getFilename()))
                .andExpect(content().bytes("Green Learner".getBytes()));

        BDDMockito.then(filerStorageService).should().downloadFile("abc.jpg");

    }

    private Resource getMockedResource() {
        Resource resource = new Resource() {
            @Override
            public boolean exists() {
                return false;
            }

            @Override
            public URL getURL() throws IOException {
                return null;
            }

            @Override
            public URI getURI() throws IOException {
                return null;
            }

            @Override
            public File getFile() throws IOException {
                File file = new File("dummy.jpeg");
                System.out.println(file.getAbsolutePath());
                return file;
            }

            @Override
            public long contentLength() throws IOException {
                return 0;
            }

            @Override
            public long lastModified() throws IOException {
                return 0;
            }

            @Override
            public Resource createRelative(String relativePath) throws IOException {
                return null;
            }

            @Override
            public String getFilename() {
                return "dummy.jpeg";
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream("Green Learner".getBytes());
            }
        };
        return resource;
    }
}
