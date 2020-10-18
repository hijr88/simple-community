package me.yh.community.repository;

import me.yh.community.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    int countByUsername(String username);

    int countByEmail(String email);

    int countByUsernameOrEmail(String username, String email);

    Optional<Member>findByUsername(String username);
}
