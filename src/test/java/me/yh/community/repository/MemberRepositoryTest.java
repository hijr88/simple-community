package me.yh.community.repository;

import me.yh.community.config.JpaAuditingConfiguration;
import me.yh.community.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(JpaAuditingConfiguration.class)
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

        int countById = memberRepository.countById("woo");
        assertEquals(1, countById);

        int countByEmail = memberRepository.countByEmail("abg");
        assertEquals(0, countByEmail);

        Optional<Member> findById = memberRepository.findById("woo");
        assertTrue(findById.isPresent());
    }

    @Test
    void findIdByEmail() {
        Optional<String> usernameByEmail = memberRepository.findIdByEmail("abc@aaa");
        assertTrue(usernameByEmail.isPresent());

        Optional<String> usernameByEmail2 = memberRepository.findIdByEmail("abc@aac");
        assertTrue(usernameByEmail2.isEmpty());
    }

    @Test
    void countByIdAndEmail() {
        int count = memberRepository.countByIdAndEmail("woo", "abc@aaa");
        assertEquals(1, count);

        int count2 = memberRepository.countByIdAndEmail("woo", "abc@aac");
        assertEquals(0, count2);
    }
}