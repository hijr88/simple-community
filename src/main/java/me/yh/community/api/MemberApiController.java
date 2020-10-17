package me.yh.community.api;

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
     * @param receiveEmail 이메일
     */
    @PostMapping(value = "/create-code", consumes = "text/plain")
    public ResponseEntity<String> sendSimpleMail(HttpServletRequest request,
                                 @RequestBody String receiveEmail) throws Exception {
        request.setCharacterEncoding("utf-8");

        boolean result = memberService.sendAuthCodeMail(request, receiveEmail);

        if(result)
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
}
