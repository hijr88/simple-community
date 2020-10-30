package me.yh.community.repository.custom;

import me.yh.community.dto.post.PostDetailDto;
import me.yh.community.dto.post.PostEditDto;
import me.yh.community.dto.post.PostListDto;
import me.yh.community.dto.post.PostPage;

import java.util.List;

public interface PostRepositoryCustom {

    List<PostListDto> findListByPageAndPub(PostPage page, boolean pub);
    Long countListByPageAndPub(PostPage page, boolean pub);

    PostDetailDto findPostDetailByIdAndPub(Long id, boolean pub);

    PostEditDto findPostEditById(Long id);
}
