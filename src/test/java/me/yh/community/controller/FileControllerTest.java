package me.yh.community.controller;

import me.yh.community.service.FileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(FileController.class)
class FileControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    FileService fileService;

    @Test
    void printThumbImage() throws Exception {
        // C:\ upload\profile\ user.png 파일이 들어 있어야 성공하는 테스트
        mockMvc.perform(get("/files/thumb/profile/woo/none")
                .param("w","50")
                .param("h","50"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Good"));

        mockMvc.perform(get("/files/thumb/profile/woo/none")
                .param("w","501")
                .param("h","50"))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(content().string("not allowed, Please check size"));

        mockMvc.perform(get("/files/thumb/profile/woo/fileName")
                .param("w","50")
                .param("h","50"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("NOT_FOUND"));
    }
}