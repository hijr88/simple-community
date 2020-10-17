package me.yh.community.api;

import me.yh.community.repository.MemberRepository;
import me.yh.community.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MemberApiController.class)
class MemberApiControllerTest {

    @Autowired MockMvc mockMvc;

    @MockBean MemberRepository memberRepository;

    @MockBean MemberService memberService;

    @DisplayName("중복되지 않은 경우")
    @Test
    void notDuplicatedUsername() throws Exception {
        String username = "woo";

        when(memberRepository.countByUsername(username)).thenReturn(0);

        mockMvc.perform(post("/api/members/check-username")
                    .contentType(MediaType.TEXT_PLAIN)
                    .content(username))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("중복인 경우")
    @Test
    void duplicatedUsername() throws Exception {
        String username = "woo";

        when(memberRepository.countByUsername(username)).thenReturn(1);

        mockMvc.perform(post("/api/members/check-username")
                .contentType(MediaType.TEXT_PLAIN)
                .content(username))
                .andDo(print())
                .andExpect(status().isConflict());
    }
}