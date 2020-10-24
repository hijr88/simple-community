package me.yh.community.entity;

import java.io.Serializable;
import java.util.Objects;


public class PostRecommendId implements Serializable {

    private Long post;
    private String member;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostRecommendId that = (PostRecommendId) o;
        return Objects.equals(post, that.post) &&
                Objects.equals(member, that.member);
    }

    @Override
    public int hashCode() {
        return Objects.hash(post, member);
    }
}
