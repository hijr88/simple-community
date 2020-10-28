package me.yh.community.service;

import java.util.Map;

public interface CommentService {
    Map<String, Object> findCommentList(long postId, long page);

    Map<String, Object> findChildCommentList(long commentId, long page);
}
