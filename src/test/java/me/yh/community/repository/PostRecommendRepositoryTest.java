package me.yh.community.repository;

import me.yh.community.config.JpaAuditingConfiguration;
import me.yh.community.entity.Member;
import me.yh.community.entity.Post;
import me.yh.community.entity.PostRecommend;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;

@DataJpaTest(includeFilters = @ComponentScan.Filter(
        type = ASSIGNABLE_TYPE,
        classes = {JpaAuditingConfiguration.class}
))
class PostRecommendRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    PostRecommendRepository postRecommendRepository;

    @Rollback(value = false)
    @Test
    void existsByPostIdAndMemberId() {

        Member member = Member.testUser("woo");
        Post post = new Post("title","content",member,0L);

        em.persist(member);
        em.persist(post);

        PostRecommend postRecommend = new PostRecommend(1L,"woo");

        em.persist(postRecommend);
    }
}