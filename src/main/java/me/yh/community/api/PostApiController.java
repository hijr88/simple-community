package me.yh.community.api;

import lombok.RequiredArgsConstructor;
import me.yh.community.repository.PostRepository;
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

    private final PostRepository postRepository;
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

    /**
     *  답글 달린 글은 삭제 불가 (0), 삭제 했으면 1
     */
    @DeleteMapping("/{postId}")
    public ResponseEntity<Integer> deletePost(@PathVariable("postId") long id) {
        boolean result = postService.deletePost(id);

        if (result) {
            return ResponseEntity.ok(1);
        } else {
            return ResponseEntity.ok(0);
        }
    }

    /**
     * 게시판 상세보기에서 공개 비공개 처리
     * @param postId 게시글 번호
     *
     */
    @PutMapping(path = "/{postId}/edit/pub", consumes = "text/plain")
    public ResponseEntity<Boolean> postChangePub(@PathVariable long postId, @RequestBody String bool) {

        boolean pub = Boolean.parseBoolean(bool);
        System.out.println(postId +", "+ bool );
        postRepository.changePubById(postId, pub);

        return ResponseEntity.ok(true);
    }
}
