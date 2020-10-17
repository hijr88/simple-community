package me.yh.community.controller;

import lombok.RequiredArgsConstructor;
import me.yh.community.entity.Member;
import me.yh.community.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
@RequestMapping("/members")
public class MemberController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final MemberService memberService;

    /**
     * @return 로그인 페이지
     */
    @GetMapping("/login")
    public String login() {
        return "members/login";
    }

    /**
     * @return 회원가입페이지
     */
    @GetMapping("/register")
    public String register() {
        return "members/register";
    }

    /**
     * 회원정보추가
     * @param member 회원정보
     *               성공시 첫 페이지로 리다이렉트
     *               실패시 에러 페이지 리다이렉트
     */
    @PostMapping("/new")
    public String createNewMember(@ModelAttribute Member member,
                                  BindingResult bindingResult,
                                  RedirectAttributes ra) {
        log.info(member.toString());
        if(bindingResult.hasErrors()){
            ra.addFlashAttribute("type","FAIL_BINDING");
            return "redirect:/error-redirect";
        }

        boolean result = memberService.createNewMember(member);

        if (result) {
            return  "redirect:/index";
        } else {
            ra.addFlashAttribute("type","FAIL_ADD_MEMBER");
            return "redirect:/error-redirect";
        }
    }
}
