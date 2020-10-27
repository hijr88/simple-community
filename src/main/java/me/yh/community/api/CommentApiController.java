package me.yh.community.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.yh.community.entity.Comment;
import me.yh.community.repository.CommentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@RequiredArgsConstructor

@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/api/comments")
public class CommentApiController {

    private final CommentRepository commentRepository;

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

    @Data
    static class CommentRequestDto {
        Long postId;
        String content;
        Long parent;
    }
}
