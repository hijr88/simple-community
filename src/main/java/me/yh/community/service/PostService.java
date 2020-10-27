package me.yh.community.service;

import me.yh.community.dto.post.PostDetailDto;
import me.yh.community.dto.post.PostRequestDto;
import me.yh.community.entity.Member;
import org.springframework.web.multipart.MultipartFile;

public interface PostService {
    boolean createNewPost(PostRequestDto newPost, Member member, MultipartFile mf);
    boolean createReplyPost(PostRequestDto newPost, Member member, MultipartFile mf);

    PostDetailDto findPostDetailByIdAndPub(long id, boolean pub);

    boolean incrementRecommend(long postId, String userName);
}