package me.yh.community.repository;

import me.yh.community.entity.PostRecommend;
import me.yh.community.entity.PostRecommendId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRecommendRepository extends JpaRepository<PostRecommend, PostRecommendId> {

    boolean existsByPostIdAndMemberId(Long postId, String memberId);
}
