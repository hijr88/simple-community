package me.yh.community.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.yh.community.entity.Comment;
import me.yh.community.repository.CommentRepository;
import me.yh.community.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RequiredArgsConstructor

@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/api/comments")
public class CommentApiController {

    private final CommentRepository commentRepository;
    private final CommentService commentService;


    /**
     * @param postId 글번호
     * @param p        댓글 페이지
     * @return 글번호에 해당하는 댓글리스트
     */
    @GetMapping(path = "/{postId}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Map<String, Object>> commentList(@PathVariable("postId") long postId
            , @RequestParam(value = "p", required = false, defaultValue = "1") long p) {

        Map<String, Object> resultMap = commentService.findCommentList(postId, p);

        return ResponseEntity.ok(resultMap);
    }

    /**
     * @param commentId 댓글번호
     * @return 댓글번호에 해당하는 답글 리스트
     */
    @GetMapping(path = "/reply/{commentId}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Map<String, Object>> getReplyCommentList(@PathVariable("commentId") long commentId
            , @RequestParam(value = "p", required = false, defaultValue = "1") long p) {

        Map<String, Object> resultMap = commentService.findChildCommentList(commentId, p);

        return ResponseEntity.ok(resultMap);
    }

    @PostMapping(path = "", consumes = "application/json")
    public ResponseEntity<Integer> addComment(Principal principal,
                                              @RequestBody CommentRequestDto c, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.ok(400);
        }
        String memberId = principal.getName();
        Comment comment = new Comment(c.postId, c.content, memberId, c.parent);
        commentRepository.save(comment);

        return ResponseEntity.ok(1);
    }

    @DeleteMapping(path = "{commentId}")
    public ResponseEntity<Integer> deleteComment(@PathVariable long commentId) {

        commentRepository.updateDeleteByCommentId(commentId);
        return ResponseEntity.ok(1);
    }

    @Data
    static class CommentRequestDto {
        Long postId;
        String content;
        Long parent;
    }
}
