package com.example.fo.controller;

import com.example.fo.service.FilerStorageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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

        MockMultipartFile multipartFile = new MockMultipartFile("multipartFile", "test-file.txt", "text/plain", "aaaa".getBytes());

        this.mockMvc.perform(MockMvcRequestBuilders.multipart("/single/upload").file(multipartFile))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

//    @Test
//    void downloadSingleFile() {
//    }

//    @Test
//    void multipleFileUpload() {
//    }

//    @Test
//    void zipDownload() {
//    }
}
