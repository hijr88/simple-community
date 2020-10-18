package me.yh.community.service;

import me.yh.community.entity.Member;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;

public interface MemberService extends UserDetailsService {

    boolean sendAuthCodeMail(HttpServletRequest request, String receiveEmail);

    boolean createNewMember(Member member);

    boolean changeTempPassword(String username, String email);
}
