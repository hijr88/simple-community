package me.yh.community.repository.custom;

import me.yh.community.dto.post.PostDetailDto;
import me.yh.community.dto.post.PostEditDto;
import me.yh.community.dto.post.PostListDto;

import java.util.List;

public interface PostRepositoryCustom {

    List<PostListDto> findPostList();

    PostDetailDto findPostDetailByIdAndPub(Long id, boolean pub);

    PostEditDto findPostEditById(Long id);
}
