package me.yh.community.repository.custom;

import me.yh.community.dto.comment.CommentList;

import java.util.List;

public interface CommentRepositoryCustom {

    List<CommentList> findCommentListById(Long postId, long offset, long limit);

    List<CommentList> findChildCommentListById(Long commentId, long offset, long limit);
}
