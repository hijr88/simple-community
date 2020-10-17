package me.yh.community.service;

import me.yh.community.entity.Member;

import javax.servlet.http.HttpServletRequest;

public interface MemberService {

    boolean sendAuthCodeMail(HttpServletRequest request, String receiveEmail);

    boolean createNewMember(Member member);
}
