package me.yh.community.security;

import me.yh.community.entity.Member;
import me.yh.community.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class SecurityTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MemberService memberService;

    @Transactional
    @Test
    void login_success() throws Exception{
        Member member = new Member("test","1234","nick","123@email","","","","");
        memberService.createNewMember(member);

        em.flush();
        em.clear();

        mockMvc.perform(formLogin("/members/login-process").user("test").password("1234"))
                .andExpect(authenticated());
    }

    @Test
    @Transactional
    void login_fail() throws Exception{
        Member member = new Member("test","1234","nick","123@email","","","","");
        memberService.createNewMember(member);

        em.flush();
        em.clear();

        mockMvc.perform(formLogin("/members/login-process").user("test").password("12345"))
                .andExpect(unauthenticated());
    }
    
}
