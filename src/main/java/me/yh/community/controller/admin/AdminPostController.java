package me.yh.community.controller.admin;

import lombok.RequiredArgsConstructor;
import me.yh.community.Utils;
import me.yh.community.dto.post.PostDetailDto;
import me.yh.community.dto.post.PostListDto;
import me.yh.community.dto.post.PostPage;
import me.yh.community.repository.PostRepository;
import me.yh.community.service.AdminService;
import me.yh.community.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/admin/posts")
@Controller
public class AdminPostController {

    private final AdminService adminService;
    private final PostService postService;
    private final PostRepository postRepository;

    @GetMapping({"","/"})
    public String list(Model model, PostPage page) {

        List<PostListDto> list = page.getContent();
        List<Boolean> isNow = isNow(list);

        String[] ids = list.stream().map(PostListDto::getId).map(String::valueOf).toArray(String[]::new);
        String allNo = String.join(" ", ids);
        System.out.printf("'%s'\n", allNo);

        model.addAttribute("allNo",allNo);
        model.addAttribute("isNow",isNow);
        model.addAttribute("page", page);
        System.out.println(page);

        return "admin/posts/list";
    }

    /**
     *  글 상세 보기
     */
    @GetMapping("/{id}")
    public String postDetail(HttpServletRequest request,
                             RedirectAttributes ra, Model model, @PathVariable long id) {

        boolean result = postRepository.existsById(id);

        if (!result) { //글이 존재 하지 않으면 에러 처리
            ra.addFlashAttribute("type","BAD_REQUEST");
            return "redirect:/error-redirect";
        }
        PostDetailDto post = postService.findPostDetailByIdAndPub(id, null);

        model.addAttribute("p", post);
        model.addAttribute("qs", Utils.getPreQS(request));
        return "admin/posts/detail";
    }


    /**
     * 체크된 글번호는 공개처리 체크 안된건 비공개처리
     * allNo    현재 페이지 모든 글번호
     * openNo   체크된 글번호
     */
    @PutMapping(path = "/edit/all-pub", consumes = "application/json")
    public ResponseEntity<Boolean> updateBoardsPub(@RequestBody Map<String, String> param) {

        String allNo = param.get("allNo");     //모든 글 번호
        String openNo = param.get("openNo");   //체크된 글 번호

        adminService.modifyPostAllPub(allNo, openNo);

        //boolean result = adminService.updateBoardAllPub(allNo, openNo);
        return ResponseEntity.ok(true);
    }


    /**
     * 게시글 생성일이 오늘인지 확인
     */
    private List<Boolean> isNow(List<PostListDto> posts) {
        List<Boolean> isNow = new ArrayList<>();

        LocalDate today = LocalDate.now();

        for (PostListDto p : posts) {
            LocalDateTime createDate = p.getCreateDate();
            LocalDate localDate = createDate.toLocalDate();
            if (ChronoUnit.DAYS.between(today, localDate) == 0)
                isNow.add(true);
            else
                isNow.add(false);
        }
        return isNow;
    }
}
