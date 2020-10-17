package me.yh.community.repository;

import me.yh.community.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    int countByUsername(String username);

    int countByEmail(String email);

    int countByUsernameOrEmail(String username, String email);
}
