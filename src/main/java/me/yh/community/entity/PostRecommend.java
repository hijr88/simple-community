package me.yh.community.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)

@IdClass(PostRecommendId.class)
@Table(name = "post_recommend")
@Entity
public class PostRecommend{

    //연관 관계보다는 그냥 하는게 더 효율성이 좋다고 판단.

    @Id
    /*@ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;*/
    @Column(name = "post_id")
    private Long postId;

    @Id
    /*@ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;*/
    @Column(name = "member_id")
    private String memberId;

    public PostRecommend(Long postId, String memberId) {
        this.postId = postId;
        this.memberId = memberId;
    }
}
