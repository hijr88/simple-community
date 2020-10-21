package me.yh.community.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
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
@Entity
@DynamicInsert                      //null 값은 insert 할 때 제외
@DynamicUpdate

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

    @ColumnDefault("1")
    private Boolean enable;

    @ColumnDefault("'ROLE_USER'")
    private String role;

    @Override
    public boolean isNew() {
        return createDate == null;
    }

    public Member(String id, String password, String nickname, String email,
                  String zoneCode, String address, String extraAddress, String detailAddress) {
        super(zoneCode, address, extraAddress, detailAddress);
        this.id = id;
        this.password = password;
        this.nickname = nickname;
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

