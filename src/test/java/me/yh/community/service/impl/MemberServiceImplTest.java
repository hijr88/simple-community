package me.yh.community.service.impl;

import me.yh.community.entity.Member;
import me.yh.community.repository.MemberRepository;
import me.yh.community.service.FileService;
import me.yh.community.service.MailService;
import me.yh.community.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @Mock
    MemberRepository memberRepository;

    @Mock
    MailService mailService;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    FileService fileService;

    @Mock
    Member member;

    MemberService memberService;

    @BeforeEach
    void Before() {
        memberService = new MemberServiceImpl(memberRepository, mailService, passwordEncoder, fileService);
        assertNotNull(memberService);
    }

    @Test
    void createNewMember() {
        //Member member = new Member("woo","1234","nick","email",null,null,null,null);

        //given
        given(memberRepository.countByUsernameOrEmail(member.getUsername(), member.getEmail())).willReturn(1);

        //when
        boolean result = memberService.createNewMember(member);

        //then
        assertFalse(result);
    }

}