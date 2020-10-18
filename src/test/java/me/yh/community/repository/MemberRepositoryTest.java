package me.yh.community.repository;

import me.yh.community.entity.Member;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import java.util.Optional;

@Rollback(value = false)
@ExtendWith(SpringExtension.class)
@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void init() {
        Member newMember1 = new Member("woo", "0806", "은별", "abc@aaa", null, null, null, null);
        memberRepository.save(newMember1);

        Member newMember2 = new Member("nara", "1315", "우리", "abc@nav", null, null, null, null);
        memberRepository.save(newMember2);

        em.flush();
        em.clear();
    }

    @Test
    void CURD() {

        int countByUsername = memberRepository.countByUsername("woo");
        assertEquals(1, countByUsername);

        int countByEmail = memberRepository.countByEmail("abg");
        assertEquals(0, countByEmail);

        Optional<Member> findByUsername = memberRepository.findByUsername("woo");
        assertTrue(findByUsername.isPresent());
    }

    @Test
    void findUsernameByEmail() {
        Optional<String> usernameByEmail = memberRepository.findUsernameByEmail("abc@aaa");
        assertTrue(usernameByEmail.isPresent());

        Optional<String> usernameByEmail2 = memberRepository.findUsernameByEmail("abc@aac");
        assertTrue(usernameByEmail2.isEmpty());
    }

    @Test
    void countByUsernameAndEmail() {
        int count = memberRepository.countByUsernameAndEmail("woo", "abc@aaa");
        assertEquals(1, count);

        int count2 = memberRepository.countByUsernameAndEmail("woo", "abc@aac");
        assertEquals(0, count2);
    }
}