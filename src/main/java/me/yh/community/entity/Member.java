package me.yh.community.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(callSuper = true)

@EntityListeners(AuditingEntityListener.class)
@DynamicInsert                      //null 값은 insert 할 때 제외
@DynamicUpdate
@Entity
public class Member extends Address implements Persistable<String > {

    @Id
    @Column(length = 50, name = "member_id", updatable = false)
    private String id;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 50, nullable = false)
    private String nickname;

    @Column(length = 300, nullable = false, unique = true, updatable = false)
    private String email;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createDate;

    @Column(length = 500)
    @ColumnDefault("'none'")
    private String profileImage;

    @ColumnDefault("'1'")
    private Boolean enable = true;

    @ColumnDefault("'ROLE_USER'")
    private String role = "ROLE_USER";

    @Override
    public boolean isNew() {
        return createDate == null;
    }

    public Member(String id, String password, String nickname, String email) {
        this.id = id;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
    }

    public static Member testUser(String id) {
        Member member = new Member();
        member.id = id;
        member.password = "pass";
        member.nickname = "테스트";
        member.email = "test@gmail";
        member.profileImage = "none";
        return member;
    }

    public static Member testUser(String id, String role) {
        Member member = new Member();
        member.id = id;
        member.password = "pass";
        member.nickname = "테스트";
        member.email = "test@gmail";
        member.profileImage = "none";
        member.role = role;
        return member;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    public void changeProfile(String nickname, String profileImage) {
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void disable() {
        this.enable = false;
    }
}

