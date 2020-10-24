package me.yh.community.controller;

import me.yh.community.entity.Member;
import me.yh.community.repository.MemberRepository;
import me.yh.community.security.WithMockCustomUser;
import me.yh.community.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired MockMvc mockMvc;
    @Mock Member member;
    @MockBean MemberRepository memberRepository;
    @MockBean MemberService memberService;

    @Test
    void login_page() throws Exception {
        mockMvc.perform(get("/members/login"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void register_page() throws Exception {
        mockMvc.perform(get("/members/register"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("회원 생성 성공하는경우")
    @Test
    void successCreateNewMember() throws Exception {
        Member member = Member.testUser("test");

        given(memberService.createNewMember(member)).willReturn(true);

        mockMvc.perform(post("/members/new")
                .flashAttr("member",member))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/index"));
    }

    @DisplayName("회원 생성 실패하는경우")
    @Test
    void failCreateNewMember() throws Exception {
        Member member = Member.testUser("test");

        given(memberService.createNewMember(member)).willReturn(false);

        mockMvc.perform(post("/members/new")
                .flashAttr("member",member))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error-redirect"));
    }

    @WithMockCustomUser
    @Test
    void me() throws Exception {
        Member member = Member.testUser("user");

        given(memberRepository.findById("user")).willReturn(Optional.of(member));

        mockMvc.perform(get("/members/me"))
                .andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    void editProfile() throws Exception {

        MockMultipartFile file = new MockMultipartFile("file", "test.txt",
                "text/plain", "hello file".getBytes());

        given(memberService.changeProfile(member, file, false)).willReturn(true);

        mockMvc.perform(multipart("/members/edit/profile").file(file)
                .param("isDelete","false")
                .flashAttr("member",member))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/members/me"));
    }
}