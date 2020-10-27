package me.yh.community.entity;

import java.io.Serializable;
import java.util.Objects;


public class PostRecommendId implements Serializable {

    private Long postId;
    private String memberId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostRecommendId that = (PostRecommendId) o;
        return Objects.equals(postId, that.postId) &&
                Objects.equals(memberId, that.memberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, memberId);
    }
}
