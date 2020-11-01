package me.yh.community.repository;

import me.yh.community.dto.member.MemberListDto;
import me.yh.community.dto.member.MemberPage;
import me.yh.community.entity.Member;
import me.yh.community.repository.custom.MemberRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    //아이디 중복 검사
    int countById(String id);
    //이메일 중복 검사
    int countByEmail(String email);
    //회원가입을 진행하기 전에 한 번 더 중복되는지 확인
    int countByIdOrEmail(String id, String email);
    //loadUserByUsername
    Optional<Member> findById(String id);
    //로그인 페이지에서 아이디 찾기 기능
    @Query("select m.id from Member m where m.email = :email and m.enable = true")
    Optional<String> findIdByEmail(@Param("email") String email);
    //로그인 페이지에서 비밀번호 찾기 기능
    @Query("select count(m) from Member m where m.id=:id and m.email=:email and m.enable=true")
    int countByIdAndEmail(@Param("id") String id, @Param("email") String email);
}
