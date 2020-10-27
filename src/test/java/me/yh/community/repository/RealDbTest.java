package me.yh.community.repository;

import me.yh.community.entity.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RealDbTest {

    @Autowired
    PostRepository postRepository;

    @Test
    void test() {
        Post post = postRepository.findById(2L).get();

        System.out.println(post.getFiles() == null);
        System.out.println(post.getFiles().getClass());
        System.out.println(post.getFiles().size());
        System.out.println(post.getFiles().getClass());


    }
}
