package me.yh.community.controller;

import lombok.RequiredArgsConstructor;
import me.yh.community.entity.Member;
import me.yh.community.repository.MemberRepository;
import me.yh.community.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Optional;

@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@Controller
@RequestMapping("/members")
public class MemberController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    /**
     * @return 로그인 페이지
     */
    @PreAuthorize("permitAll()")
    @GetMapping("/login")
    public String login() {
        return "members/login";
    }

    /**
     * @return 회원가입페이지
     */
    @PreAuthorize("permitAll()")
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
    @PreAuthorize("permitAll()")
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

    /**
     * 내정보 페이지
     *
     * @param principal 회원아이디 조회하고
     * @return 회원정보 페이지로 리턴
     */
    @GetMapping("/me")
    public String me(Principal principal, Model model) {

        Optional<Member> findMember = memberRepository.findByUsername(principal.getName());
        findMember.ifPresent( member -> model.addAttribute("m",member));
        return "members/me";
    }


    /**
     * 내정보 수정페이지
     *
     * @param principal 회원아이디 조회하고
     * @return 회원수정 페이지로
     */
    @GetMapping("/edit/{type}")
    public String  editProfile(Model model, Principal principal, @PathVariable("type") String type) {

        Optional<Member> findMember = memberRepository.findByUsername(principal.getName());
        findMember.ifPresent( member -> model.addAttribute("m",member));

        model.addAttribute("type",type);

        switch (type) {
            case "profile":
                model.addAttribute("title","프로필 수정");
                break;
            case "password":
                model.addAttribute("title", "비밀번호 수정");
                break;
            case "address":
                model.addAttribute("title", "주소 수정");
                break;
        }

        return "members/edit";
    }

    /**
     * 총 3가지 경우의 수
     * 1. 파일 삭제 & 닉네임 수정
     * 2. 파일 수정 & 닉네임 수정
     * 3. 파일 수정 없이 닉네임만 수정
     * 닉네임은 입력 하든 안하든 값이 들어옴.
     *
     * name, profile 수정
     * @return 프로필 수정이 완료 되면 리턴 1
     */
    @PostMapping("/edit/profile")
    public String editProfile(RedirectAttributes ra,
                              @ModelAttribute Member editMember,
                              @RequestParam(name = "file") MultipartFile mf,
                              @RequestParam(name = "isDelete") boolean isDelete) {

        boolean result = memberService.changeProfile(editMember, mf, isDelete);

        if (result) {
            return "redirect:/members/me";
        } else {
            ra.addFlashAttribute("type","FAIL_MODIFY_MEMBER");
            return "redirect:/error-redirect";
        }
    }

    /**
     * @return 패스워드 변경 처리
     */
    @PostMapping("/edit/password")
    public String editPassword(RedirectAttributes ra ,@RequestParam String password) {

        boolean result = memberService.changePassword(password);

        if (result) {
            return "redirect:/members/me";
        } else {
            ra.addFlashAttribute("type","FAIL_MODIFY_MEMBER");
            return "redirect:/error-redirect";
        }
    }

    /**
     * @return 주소 변경 처리
     */
    @PostMapping("/edit/address")
    public String editAddress(RedirectAttributes ra, @ModelAttribute Member member) {

        boolean result = memberService.changeAddress(member);

        if (result) {
            return "redirect:/members/me";
        } else {
            ra.addFlashAttribute("type","FAIL_MODIFY_MEMBER");
            return "redirect:/error-redirect";
        }
    }

    /**
     *  회원 탈퇴 처리
     */
    @PreAuthorize("isAuthenticated() and not hasRole('MASTER')")
    @GetMapping("/drop")
    public String drop() {
        return "members/drop";
    }

    @PostMapping("/drop")
    public String drop(HttpServletRequest request, RedirectAttributes ra) {
        boolean result = memberService.disable(request);

        if (result) {
            return "redirect:/index";
        } else {
            ra.addFlashAttribute("type","FAIL_MODIFY_MEMBER");
            return "redirect:/error-redirect";
        }
    }
}
