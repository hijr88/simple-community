package me.yh.community.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(HomeController.class)
class HomeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @WithAnonymousUser
    @Test
    void index() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("인증된 상태에서 접근 가능한지")
    void principal_success() throws Exception {
        mockMvc.perform(get("/pra"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    @DisplayName("인증 안된 상태에서 접근에 실패하는지 = 리다이렉트")
    void principal_fail() throws Exception {
        mockMvc.perform(get("/pra"))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }

}