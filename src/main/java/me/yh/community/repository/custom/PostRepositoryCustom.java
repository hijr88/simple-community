package me.yh.community.repository.custom;

import me.yh.community.dto.post.PostDetailDto;
import me.yh.community.dto.post.PostEditDto;
import me.yh.community.dto.post.PostListDto;
import me.yh.community.dto.post.PostPage;

import java.util.List;

public interface PostRepositoryCustom {

    List<PostListDto> findListByPageAndPub(PostPage page, Boolean pub);
    Long countListByPageAndPub(PostPage page, Boolean pub);

    PostDetailDto findPostDetailByIdAndPub(Long id, Boolean pub);

    PostEditDto findPostEditById(Long id);
}
