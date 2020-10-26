package me.yh.community.repository;

import me.yh.community.config.JpaAuditingConfiguration;
import me.yh.community.entity.Member;
import me.yh.community.entity.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;

@DataJpaTest(includeFilters = @ComponentScan.Filter(
        type = ASSIGNABLE_TYPE,
        classes = {JpaAuditingConfiguration.class}
))
class PostRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    PostRepository postRepository;
    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void init() {
        Member member = createUser();
        Post post1 = new Post("첫 글임다", "하이여~", member, 0L, 1L, 0, 0 );
        Post post2 = new Post("두번째!", "하욤하욤~", member, 0L, 2L, 0, 0 );
        Post post3 = new Post("3번째!", "1234~", member, 0L, 3L, 0, 0 );
        Post post4 = new Post("4번째!", "1234~", member, 0L, 4L, 0, 0 );
        Post post5 = new Post("1-1", "1234~", member, 1L, 1L, 1, 1 );
        Post post7 = new Post("1-1-1", "1234~", member, 5L, 1L, 2, 2 );
        Post post8 = new Post("1-1-2", "1234~", member, 5L, 1L, 3, 2 );
        Post post6 = new Post("1-2", "1234~", member, 1L, 1L, 4, 1 );
        Post post10 = new Post("1-2-1", "1234~", member, 6L, 1L, 5, 2 );
        Post post9 = new Post("1-3", "1234~", member, 1L, 1L, 6, 2 );
        Post post11 = new Post("1-4", "1234~", member, 1L, 1L, 7, 1 );

        Post[] posts = {post1,post2,post3,post4,post5,post6,post7,post8,post9,post10,post11};

        for (Post post : posts) {
            postRepository.save(post);
        }
    }


    @Test
    void save() {
        Member member = createUser();
        Post post = Post.builder().title("제목").content("내용").member(member).build();

        Post save = postRepository.save(post);

        assertNotNull(save.getId());
    }

    @Test
    void existsById() {
        Member member = createUser();
        Post post = Post.builder().title("제목").content("내용").member(member).build();
        Post save = postRepository.save(post);

        boolean result = postRepository.existsById(save.getId());

        assertTrue(result);
    }


    @DisplayName("부모글이 가진 자식글중 가장 큰 그룹순서 번호")
    @Test
    void findLastChildOrderById() {

        Long id1 = 1L;
        Long id2 = 5L;

        //1의 마지막 자식글 번호는 4번 4번의 순서는 7
        Integer order1 = postRepository.findLastChildOrderById(id1);
        //5의 마지막 자식글 번호는 8번 8번의 순서는 3
        Integer order2 = postRepository.findLastChildOrderById(id2);

        assertEquals(7, order1);
        assertEquals(3, order2);
    }

    @Test
    void incrementGroupOrder() {

        postRepository.incrementGroupOrder(1L, 0);
        //서비스에서는 조회를 안하지만 여기선 테스트를 위해 하므로 클리어 해야함
        em.clear();

        Optional<Post> find = postRepository.findById(11L);
        Post post = find.get();

        assertEquals(8 ,post.getGroupOrder());
    }




    private Member createUser() {
        Member member = Member.testUser("test");

        return memberRepository.save(member);
    }
}