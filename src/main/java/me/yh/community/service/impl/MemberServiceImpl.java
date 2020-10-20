package me.yh.community.service.impl;

import lombok.RequiredArgsConstructor;
import me.yh.community.Utils;
import me.yh.community.config.security.CustomUser;
import me.yh.community.entity.Member;
import me.yh.community.repository.MemberRepository;
import me.yh.community.service.FileService;
import me.yh.community.service.MailService;
import me.yh.community.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberServiceImpl implements MemberService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final MemberRepository memberRepository;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final FileService fileService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> findMember = memberRepository.findByUsername(username);
        if (findMember.isEmpty()) {
            throw new UsernameNotFoundException("사용자가 입력한 아이디에 해당하는 사용자를 찾을 수 없습니다.");
        }
        return new CustomUser(findMember.get());
    }

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

    @Transactional
    @Override
    public boolean changeTempPassword(String username, String email) {

        //임시 비밀번호 생성
        String tempPassword = Utils.createRandomCode();

        Optional<Member> findMember = memberRepository.findByUsername(username);
        if(findMember.isEmpty())
            return false;

        Member member = findMember.get();
        String encodePassword = passwordEncoder.encode(tempPassword);
        member.changePassword(encodePassword);

        String sb = "<html>" +
                "<body>" +
                "    <div style=\"border: 2px solid rgb(77, 194, 125); display: inline-block;padding: 2px 5px;\">" +
                "    <p>변경된 비밀번호입니다.</p>" +
                "    <p>비밀번호:  <span style=\"font-size: 1.3rem; color: #1d80fb; font-weight: bold; text-decoration: underline;\">" + tempPassword + "</span></p>" +
                "    </div>" +
                "</body>" +
                "</html>";
        mailService.sendMail(email, "임시 비밀번호 발급.", sb); //받는사람이메일주소,제목,내용

        return true;
    }

    @Transactional
    @Override
    public boolean changeProfile(Member editMember, MultipartFile mf, boolean isDelete) {
        CustomUser customUser = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username = customUser.getUsername();

        Optional<Member> findMember = memberRepository.findByUsername(username);
        Member member;

        if (findMember.isPresent())
            member = findMember.get();
        else {
            return false;
        }

        String folderPath = FileService.profilePath + File.separator + username;
        String filePath   = folderPath + File.separator + member.getProfileImage();

        // 1. 파일 삭제
        if (isDelete) {
            if (!fileService.deleteFile(filePath)) {
                return false;
            }
            member.changeProfile(editMember.getNickname(), "none");
            customUser.setProfileImage("none");
        }
        // 2. 파일 수정
        else if (mf.getSize() != 0) {
            String newFileName = fileService.upload(folderPath, mf);
            if (newFileName == null)  {
                return false;
            }
            // 업로드 됐으면 진행
            if (!member.getProfileImage().equals("none")) //기본 프로필이 아닌경우에만 삭제 진행
                fileService.deleteFile(filePath);
            member.changeProfile(editMember.getNickname(), newFileName);
            customUser.setProfileImage(Utils.urlEncode(newFileName));
        }
        // 3. 닉네임만 수정인 경우
        else {
            //닉네임만 교체, 프로필은 그대로
            member.changeProfile(editMember.getNickname(), member.getProfileImage());
        }

        return true;
    }

    @Override
    public boolean isMatchPassword(String password) {
        Member member = getMember();
        if (member == null) return false;

        //입력한 비밀번호와 회원 비밀번호가 일치하는지
        return passwordEncoder.matches(password, member.getPassword());
    }

    @Transactional
    @Override
    public boolean changePassword(String password) {
        Member member = getMember();
        if (member == null) return false;

        member.changePassword(passwordEncoder.encode(password));
        return true;
    }

    @Transactional
    @Override
    public boolean changeAddress(Member editMember) {
        Member member = getMember();
        if (member == null) return false;

        member.changeFullAddress(editMember.getZoneCode(), editMember.getAddress(), editMember.getExtraAddress(), editMember.getDetailAddress());
        return true;
    }

    @Transactional
    @Override
    public boolean disable(HttpServletRequest request) {
        Member member = getMember();
        if (member == null) return false;

        member.disable();
        HttpSession session = request.getSession();
        session.invalidate();

        return true;
    }

    private Member getMember() {
        CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //회원 정보 가져오기
        Optional<Member> findMember = memberRepository.findByUsername(user.getUsername());
        //회원 정보 없으면 false
        if (findMember.isEmpty()) return null;

        return findMember.get();
    }
}
