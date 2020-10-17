package me.yh.community.service.impl;

import lombok.RequiredArgsConstructor;
import me.yh.community.Utils;
import me.yh.community.entity.Member;
import me.yh.community.repository.MemberRepository;
import me.yh.community.service.MailService;
import me.yh.community.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final MemberRepository memberRepository;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;


    @Override
    public boolean sendAuthCodeMail(HttpServletRequest request, String receiveEmail) {
        try {
            //인증코드 생성
            String randomCode = Utils.createRandomCode();
            //인증코드 세션 바인딩
            HttpSession session = request.getSession();
            session.setAttribute("code_", randomCode);

            String sb =
                    "<html>" +
                            "<body>" +
                            "    <div style=\"border: 2px solid rgb(77, 194, 125); display: inline-block;padding: 2px 5px;\">" +
                            "    <p>회원가입 인증번호 입니다.</p>" +
                            "    <p>인증번호:  <span style=\"font-size: 1.3rem; color: #1d80fb; font-weight: bold; text-decoration: underline;\">" + randomCode + "</span></p>" +
                            "    </div>" +
                            "</body>" +
                            "</html>";
            mailService.sendMail(receiveEmail, "회원가입 인증번호입니다.", sb); //받는사람이메일주소,제목,내용
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *  회원가입을 진행하기 전에 한 번 더 중복되는지 확인한다.
     */
    @Override
    public boolean createNewMember(Member member) {
        int result = memberRepository.countByUsernameOrEmail(member.getUsername(), member.getEmail());

        if (result == 1) {
            return false;
        } else {
            member.encodePassword(passwordEncoder);
            if(member.getZoneCode().equals(""))
                member.changeFullAddress(null,null,null,null);
            memberRepository.save(member);
            return true;
        }
    }
}
