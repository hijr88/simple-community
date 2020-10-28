package me.yh.community.service.impl;

import lombok.RequiredArgsConstructor;
import me.yh.community.repository.CommentRepository;
import me.yh.community.service.CommentService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    long limit = 10L;

    @Override
    public Map<String, Object> findCommentList(long postId, long page) {
        Map<String, Object> resultMap = new HashMap<>();

        long offset = (page -1) * 10;

        resultMap.put("list",commentRepository.findCommentListById(postId, offset, limit));
        resultMap.put("count",commentRepository.countByPostIdAndParent(postId, 0L));
        resultMap.put("totalCount", commentRepository.countByPostId(postId));

        return resultMap;
    }

    @Override
    public Map<String, Object> findChildCommentList(long commentId, long page) {

        Map<String, Object> resultMap = new HashMap<>();

        long offset = (page -1) * 10;

        resultMap.put("list", commentRepository.findChildCommentListById(commentId, offset, limit));
        resultMap.put("count", commentRepository.countByPostId(commentId));

        return resultMap;
    }
}
