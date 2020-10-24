package me.yh.community.repository;

import me.yh.community.entity.Member;
import me.yh.community.entity.Post;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataJpaTest
class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;
    @Autowired
    MemberRepository memberRepository;

    @BeforeAll
    void saveMember() {
        Member member = Member.testUser("test");

        memberRepository.save(member);
    }

    @Test
    void save() {
        Member member = Member.testUser("test");

        Post post = Post.createNewPost("제목", "내용", member);

        postRepository.save(post);

    }

}