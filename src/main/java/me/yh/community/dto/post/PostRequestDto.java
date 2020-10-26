package me.yh.community.dto.post;

import lombok.Data;
import me.yh.community.entity.Member;
import me.yh.community.entity.Post;

@Data
public class PostRequestDto {

    String title;
    String content;
    Long parent;

    public PostRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static Post createNewPost(PostRequestDto newPost, Member member) {
        return Post.builder().title(newPost.title)
                                .content(newPost.content)
                                .member(member).build();
    }

    public static Post createReplyPost(PostRequestDto newPost, Member member) {
        return Post.builder().title(newPost.title)
                .content(newPost.content)
                .member(member)
                .parent(newPost.parent)
                        .build();
    }
}
