package me.yh.community.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)

@EntityListeners(AuditingEntityListener.class)
@IdClass(PostRecommendId.class)
@Entity
public class PostRecommend{

    @Id
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Id
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
