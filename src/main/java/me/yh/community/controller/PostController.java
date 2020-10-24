package me.yh.community.controller;

import lombok.RequiredArgsConstructor;
import me.yh.community.dto.PostPage;
import me.yh.community.entity.Member;
import me.yh.community.entity.Post;
import me.yh.community.repository.MemberRepository;
import me.yh.community.repository.PostRepository;
import me.yh.community.service.PostService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Optional;

@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
@RequestMapping("/posts")
@Controller
public class PostController {

    private final PostRepository postRepository;
    private final PostService postService;
    private final MemberRepository memberRepository;


    @PreAuthorize("permitAll()")
    @GetMapping(path = {"","/"})
    public String index(Model model, PostPage page){
        //List<Board> list = boardRepository.findAllByOrderByCreateDateDesc();
        //model.addAttribute("m",list);

        System.out.println(page);
        model.addAttribute("page", page);

        return "posts/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {

        model.addAttribute("title", "새 글 쓰기");
        model.addAttribute("action","new");

        return "posts/form";
    }

    @PostMapping("/new")
    public String createPost(RedirectAttributes ra,
                             @RequestParam(name = "input-file", required = false) MultipartFile mf,
                             @ModelAttribute Post newPost,
                             Principal principal) {
        Optional<Member> findMember = memberRepository.findById(principal.getName());

        if (findMember.isEmpty()) {
            ra.addFlashAttribute("type","FAIL_ADD_POST");
            return "redirect:/error-redirect";
        }
        Member member = findMember.get();

        boolean result = postService.createNewPost(newPost, member, mf);

        if (result) {
            return "redirect:/posts";
        } else {
            ra.addFlashAttribute("type","FAIL_ADD_POST");
            return "redirect:/error-redirect";
        }
    }
}
