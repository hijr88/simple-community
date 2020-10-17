package me.yh.community.controller;

import me.yh.community.entity.Member;
import me.yh.community.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired MockMvc mockMvc;
    @MockBean MemberService memberService;
    @Mock Member member;

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
        Member member = new Member("user","pass","nick","email","","","","");

        when(memberService.createNewMember(member)).thenReturn(true);

        mockMvc.perform(post("/members/new")
                .flashAttr("member",member))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/index"));
    }

    @DisplayName("회원 생성 실패하는경우")
    @Test
    void failCreateNewMember() throws Exception {
        Member member = new Member("user","pass","nick","email","","","","");

        when(memberService.createNewMember(member)).thenReturn(false);

        mockMvc.perform(post("/members/new")
                .flashAttr("member",member))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error-redirect"));
    }
}