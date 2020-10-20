package me.yh.community.repository;

import me.yh.community.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    //아이디 중복 검사
    int countByUsername(String username);
    //이메일 중복 검사
    int countByEmail(String email);
    //회원가입을 진행하기 전에 한 번 더 중복되는지 확인
    int countByUsernameOrEmail(String username, String email);
    //loadUserByUsername
    Optional<Member>findByUsername(String username);
    //로그인 페이지에서 아이디 찾기 기능
    @Query("select m.username from Member m where m.email = :email and m.enable = true")
    Optional<String> findUsernameByEmail(@Param("email") String email);
    //로그인 페이지에서 비밀번호 찾기 기능
    @Query("select count(m) from Member m where m.username=:username and m.email=:email and m.enable=true")
    int countByUsernameAndEmail(@Param("username") String username, @Param("email") String email);
}
