package me.yh.community.repository;

import me.yh.community.entity.Member;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Rollback(value = false)
    @Test
    void CURD() {

        Member newMember = new Member("woo", "0806", "은별", "abc@aaa", null, null, null, null);
        memberRepository.save(newMember);

        em.flush();
        em.clear();

        Optional<Member> findById = memberRepository.findById(newMember.getId());
        assertTrue(findById.isPresent());

        int countByUsername = memberRepository.countByUsername("woo");
        assertEquals(1, countByUsername);

        int countByEmail = memberRepository.countByEmail("abg");
        assertEquals(0, countByEmail);

        Optional<Member> findByUsername = memberRepository.findByUsername("woo");
        assertTrue(findByUsername.isPresent());
    }
}