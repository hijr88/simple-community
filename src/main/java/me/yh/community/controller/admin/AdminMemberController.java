package me.yh.community.controller.admin;

import lombok.RequiredArgsConstructor;
import me.yh.community.Utils;
import me.yh.community.dto.member.MemberPage;
import me.yh.community.entity.Member;
import me.yh.community.repository.MemberRepository;
import me.yh.community.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor

@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin/members")
@Controller
public class AdminMemberController {

    private final MemberRepository memberRepository;
    private final AdminService adminService;


    /**
     * @param page 페이지 정보
     * @return 회원 리스트
     */
    @GetMapping(path = {"","/"})
    public String members(Model model, MemberPage page) {

        System.out.println(page);

        model.addAttribute("page",page);
        return "admin/members/list";
    }

    /**
     * @param memberId 회원 아이디
     * @return 아이디에 해당하는 회원 정보 페이지
     */
    @GetMapping(path = "{memberId}")
    public String member(Model model, @PathVariable String memberId, HttpServletRequest request ) {

        Optional<Member> byId = memberRepository.findById(memberId);
        if (byId.isEmpty()) {
            return "redirect:/admin/members";
        }
        Member member = byId.get();
        member.setProfileImage(Utils.urlEncode(member.getProfileImage()));
        switch (member.getRole()) {
            case "ROLE_MASTER" : member.setRole("마스터");break;
            case "ROLE_ADMIN" : member.setRole("관리자");break;
            case "ROLE_USER" : member.setRole("사용자");break;
        }
        model.addAttribute("member",member);
        model.addAttribute("qs", Utils.getPreQS(request));

        return "admin/members/member";
    }

    /**
     * @param id  회원 아이디
     * @param map enable or role
     * @return 수정 됐으면 1 실패 0
     */
    @PutMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<Integer> updateMember(@PathVariable("id") String id
            , @RequestBody Map<String, Object> map) {

        boolean result = adminService.modifyMember(id, map);

        if (result) {
            return ResponseEntity.ok(1);
        } else
            return ResponseEntity.ok(0);
    }
}
