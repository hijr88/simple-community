package me.yh.community.api;

import lombok.RequiredArgsConstructor;
import me.yh.community.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor

@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/api/posts")
public class PostApiController {

    private final PostService postService;

    /**
     * @return 추천수를 올렸으면 1 이미 올린 상태면 0
     */
    @GetMapping("/{postId}/recommend")
    public ResponseEntity<Integer> upRecommend(@PathVariable("postId") long postId, Principal principal) {
        String userName = principal.getName();

        boolean result = postService.incrementRecommend(postId, userName);
        if (result) { //성공적으로 추천수를 올리면 1
            return ResponseEntity.ok(1);
        } else { //이미 올린 회원이면 0
            return ResponseEntity.ok(0);
        }
    }
}
