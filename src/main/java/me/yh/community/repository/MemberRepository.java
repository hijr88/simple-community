package me.yh.community.repository;

import me.yh.community.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    int countByUsername(String username);

    int countByEmail(String email);

    int countByUsernameOrEmail(String username, String email);

    Optional<Member>findByUsername(String username);

    @Query("select m.username from Member m where m.email = :email")
    Optional<String> findUsernameByEmail(@Param("email") String email);

    int countByUsernameAndEmail(String username, String email);
}
