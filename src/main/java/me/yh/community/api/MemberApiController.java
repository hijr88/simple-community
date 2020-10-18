package me.yh.community.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.yh.community.repository.MemberRepository;
import me.yh.community.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/members")
public class MemberApiController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    /**
     * @param username 유저 아이디
     * @return OK: 사용가능한 아이디, CONFLICT: 중복된 아이디
     */
    @PostMapping(value = "/check-username", consumes = "text/plain")
    public ResponseEntity<Void> isDuplicatedUsername(@RequestBody String username) {
        log.info("아이디 중복검사: '{}'", username);
        int result = memberRepository.countByUsername(username);
        if (result == 0)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    /**
     * @param email 이메일
     * @return OK: 사용가능한 이메일, CONFLICT: 중복된 이메일
     */
    @PostMapping(path = "/check-email", consumes = "text/plain")
    public ResponseEntity<Void> isDuplicatedEmail(@RequestBody String email) {
        log.info("이메일 중복검사: '{}'", email);
        int result = memberRepository.countByEmail(email);
        if (result == 0)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    /**
     * 해당 이메일에 인증코드 전송
     *
     * @param receiveEmail 이메일
     */
    @PostMapping(value = "/create-code", consumes = "text/plain")
    public ResponseEntity<String> sendSimpleMail(HttpServletRequest request,
                                                 @RequestBody String receiveEmail) throws Exception {
        request.setCharacterEncoding("utf-8");

        boolean result = memberService.sendAuthCodeMail(request, receiveEmail);

        if (result)
            return ResponseEntity.ok("1");
        else
            return ResponseEntity.ok("0");
    }

    /**
     * 회원 가입 페이지에서 사용
     *
     * @param code 사용자가 입력한 인증번호
     * @return 1:일치 0:불일치
     */
    @PostMapping(value = "/check-code")
    public ResponseEntity<String> checkCode(@RequestBody String code,
                                            HttpServletRequest request) {
        log.info("'{}'", code);
        HttpSession session = request.getSession();
        String code_ = (String) session.getAttribute("code_");
        if (code_ == null) {
            return new ResponseEntity<>("2", HttpStatus.OK); //서버가 재시작하여 세션이 날라간경우
        }
        if (code_.equals(code)) {
            //입력한 인증코드와 실제 인증코드가 일치하면 1전송
            session.removeAttribute("code_");
            return ResponseEntity.ok("1");
        } else
            return ResponseEntity.ok("0");
    }

    /**
     * 로그인 페이지에서 아이디 찾기 기능
     *
     * @param email 이메일
     * @return 이메일에 일치하는 아이디, 없으면 null
     */
    @PostMapping(path = "/find-username", consumes = "text/plain")
    public ResponseEntity<String> findUsername(@RequestBody String email) {
        Optional<String> username = memberRepository.findUsernameByEmail(email);

        String result = "";
        if (username.isPresent())
            result = username.get();

        return ResponseEntity.ok(result);
    }

    /**
     * 로그인 페이지에서 비밀번호 찾기 기능
     *
     * @param member 아이디 & 이메일

     * @return 아이디와 이메일이 일치하는 회원수 1, 불일치 0
     */
    @PostMapping(path = "/find-member", consumes = "application/json")
    public ResponseEntity<Integer> findMember(@RequestBody MemberUsernameAndEmail member) {
        int result = memberRepository.countByUsernameAndEmail(member.getUsername(), member.getEmail());
        return ResponseEntity.ok(result);
    }

    /**
     * 로그인 페이지에서 비밀빈호 찾기 후 새로운 비밀번호 발급
     *
     * @param member    아이디: 임시 비밀번호로 대체할 아이디 & 이메일: 임시 비밀번호를 받을 이메일
     */
    @PostMapping(path = "/change-temp-password", consumes = "application/json")
    public ResponseEntity<String> changeTempPassword(@RequestBody MemberUsernameAndEmail member) {
        boolean result = memberService.changeTempPassword(member.getUsername(), member.getEmail());
        if (result)
            return ResponseEntity.ok("OK");
        else
            return ResponseEntity.ok("FAIL");
    }

    @Data
    static class MemberUsernameAndEmail {
        private String username;
        private String email;
    }
}
