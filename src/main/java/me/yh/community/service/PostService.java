package me.yh.community.service;

import me.yh.community.entity.Member;
import me.yh.community.entity.Post;
import org.springframework.web.multipart.MultipartFile;

public interface PostService {
    boolean createNewPost(Post newPost, Member member, MultipartFile mf);
}
