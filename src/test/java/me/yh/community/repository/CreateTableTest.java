package me.yh.community.repository;

import me.yh.community.entity.Post;
import me.yh.community.entity.Member;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class CreateTableTest {

    @PersistenceContext
    EntityManager em;

    @Test
    void test() {


    }
}
