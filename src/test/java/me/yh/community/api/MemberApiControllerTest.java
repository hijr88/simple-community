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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MemberApiController.class)
class MemberApiControllerTest {

    @Autowired MockMvc mockMvc;

    @MockBean MemberRepository memberRepository;

    @MockBean MemberService memberService;

    @DisplayName("회원가입창에서 아이디가 중복되지 않은 경우")
    @Test
    void notDuplicatedUsername() throws Exception {
        String username = "woo";

        given(memberRepository.countByUsername(username)).willReturn(0);

        mockMvc.perform(post("/api/members/check-username")
                    .contentType(MediaType.TEXT_PLAIN)
                    .content(username))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("회원가입창에서 아이디가 중복인 경우")
    @Test
    void duplicatedUsername() throws Exception {
        String username = "woo";

        given(memberRepository.countByUsername(username)).willReturn(1);

        mockMvc.perform(post("/api/members/check-username")
                .contentType(MediaType.TEXT_PLAIN)
                .content(username))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @DisplayName("로그인창에서 아이디 찾기")
    @Test
    void findUsername() throws Exception {
        String username = "woo";

        given(memberRepository.findUsernameByEmail("email")).willReturn(Optional.of(username));

        mockMvc.perform(post("/api/members/find-username")
                .contentType(MediaType.TEXT_PLAIN)
                .content("email"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(username));

        mockMvc.perform(post("/api/members/find-username")
                .contentType(MediaType.TEXT_PLAIN)
                .content("email2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @DisplayName("로그인창에서 패스워드 찾기 요청")
    @Test
    void findMember() throws Exception {
        String json = "{\"username\" : \"woo\", \"email\" : \"email\"}";

        given(memberRepository.countByUsernameAndEmail("woo","email")).willReturn(1);

        mockMvc.perform(post("/api/members/find-member")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @DisplayName("로그인창에서 새로운 패스워드 요청")
    @Test
    void changeTempPassword() throws Exception {
        String json = "{\"username\" : \"woo\", \"email\" : \"email\"}";

        given(memberService.changeTempPassword("woo","email")).willReturn(true);

        mockMvc.perform(post("/api/members/change-temp-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }

    @DisplayName("패스워드 일치 요청")
    @WithMockUser
    @Test
    void matchPassword() throws Exception {
        String password = "1234";

        given(memberService.isMatchPassword(password)).willReturn(true);

        mockMvc.perform(post("/api/members/match-password")
                .contentType(MediaType.TEXT_PLAIN)
                .content(password))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }
}