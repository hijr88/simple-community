package me.yh.community.controller;

import lombok.RequiredArgsConstructor;
import me.yh.community.Utils;
import me.yh.community.dto.post.*;
import me.yh.community.entity.Member;
import me.yh.community.repository.MemberRepository;
import me.yh.community.repository.PostRepository;
import me.yh.community.service.PostService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
@RequestMapping("/posts")
@Controller
public class PostController {

    private final PostRepository postRepository;
    private final PostService postService;
    private final MemberRepository memberRepository;
    
    /**
     * 글 게시판 리스트
     */
    @PreAuthorize("permitAll()")
    @GetMapping(path = {"","/"})
    public String index(Model model, PostPage page){

        PostPage result = postService.findPostList(page);

        List<PostListDto> list = result.getContent();
        List<Boolean> isNow = isNow(list);

        model.addAttribute("isNow",isNow);
        model.addAttribute("page", page);
        System.out.println(page);

        return "posts/list";
    }

    /**
     *  글 입력 폼
     */
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("title", "새 글 쓰기");
        model.addAttribute("action","new");

        return "posts/form";
    }

    /**
     * 글 추가하기
     */
    @PostMapping("/new")
    public String createPost(RedirectAttributes ra,
                             @RequestParam(name = "input-file", required = false) MultipartFile mf,
                             @ModelAttribute PostRequestDto newPost, BindingResult bindingResult,
                             Principal principal) {
        if (bindingResult.hasErrors()) {
            ra.addFlashAttribute("type","FAIL_BINDING");
            return "redirect:/error-redirect";
        }

        Optional<Member> findMember = memberRepository.findById(principal.getName());
        if (findMember.isEmpty()) {
            ra.addFlashAttribute("type","FAIL_ADD_POST");
            return "redirect:/error-redirect";
        }
        Member member = findMember.get();

        boolean result;
        if (newPost.getParent() == null) {
            result = postService.createNewPost(newPost, member, mf);
        } else {
            result = postService.createReplyPost(newPost, member, mf);
        }

        if (result) {
            return "redirect:/posts";
        } else {
            ra.addFlashAttribute("type","FAIL_ADD_POST");
            return "redirect:/error-redirect";
        }
    }

    /**
     * 부모글에 대한 답글 입력 폼
     */
    @GetMapping("/{parentId}/reply")
    public String replyForm(RedirectAttributes ra, Model model, @PathVariable("parentId") long parentId) {

        boolean result = postRepository.existsById(parentId);
        
        if (!result) { //부모글이 존재 하지 않으면 에러 처리
            ra.addFlashAttribute("type","BAD_REQUEST");
            return "redirect:/error-redirect";
        }

        model.addAttribute("title", "답 글 쓰기");
        model.addAttribute("action","new");
        model.addAttribute("parent",parentId);

        return "posts/form";
    }

    /**
     *  글 상세 보기
     */
    @GetMapping("/{id}")
    public String postDetail(HttpServletRequest request,
                             RedirectAttributes ra, Model model, @PathVariable long id) {

        boolean result = postRepository.existsByIdAndPub(id, true);

        if (!result) { //부모글이 존재 하지 않으면 에러 처리
            ra.addFlashAttribute("type","BAD_REQUEST");
            return "redirect:/error-redirect";
        }
        PostDetailDto post = postService.findPostDetailByIdAndPub(id, true);

        model.addAttribute("p", post);
        model.addAttribute("qs", Utils.getPreQS(request));
        return "posts/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(Model model, RedirectAttributes ra,
                           @PathVariable long id, Principal principal) {

        String username = principal.getName();

        model.addAttribute("title", "수정하기");
        model.addAttribute("action",id + "/edit");

        PostEditDto post = postRepository.findPostEditById(id);
        if (post == null || !post.getMemberId().equals(username)) { // 비공개 글이거나 작성자와 아이디가 다른경우
            ra.addFlashAttribute("type","BAD_REQUEST");
            return "redirect:/error-redirect";
        }

        model.addAttribute("post",post);
        return "posts/form";
    }

    @PostMapping("{id}/edit")
    public String modifyPost(RedirectAttributes ra,
                             @RequestParam(name = "input-file", required = false) MultipartFile mf,
                             @RequestParam("isDelete") boolean isDelete,
                             @ModelAttribute PostRequestDto editPost, BindingResult bindingResult,
                             @PathVariable long id) {
        if (bindingResult.hasErrors()) {
            ra.addFlashAttribute("type","FAIL_BINDING");
            return "redirect:/error-redirect";
        }
        boolean result = postService.modify(id, editPost, mf, isDelete);
        if (result) {
            return "redirect:/posts/" + id;
        }

        ra.addFlashAttribute("type","FAIL_MODIFY_POST");
        return "redirect:/error-redirect";
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
