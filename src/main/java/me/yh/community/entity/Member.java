package me.yh.community.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(callSuper = true)

@Entity
@DynamicInsert                      //null 값은 insert 할 때 제외
@SequenceGenerator(
        name="member_seq_gen",      //시퀀스 제너레이터 이름
        sequenceName="member_seq",  //시퀀스 이름
        initialValue=1,             //시작값
        allocationSize=1            //메모리를 통해 할당할 범위 사이즈
)
public class Member extends Address{

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE,
                        generator = "member_seq_gen")
    @Column(name = "member_id")
    private Long id;

    @Column(length = 50, nullable = false, unique = true, updatable = false)
    private String username;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 50, nullable = false)
    private String nickname;

    @Column(length = 300, nullable = false, unique = true, updatable = false)
    private String email;

    @Column(updatable = false)
    private LocalDateTime createDate;

    @Column(length = 500)
    private String profileImage;

    @ColumnDefault("1")
    private Boolean enable;

    @ColumnDefault("'ROLE_USER'")
    private String role;

    public Member(String username, String password, String nickname, String email,
                  String zoneCode, String address, String extraAddress, String detailAddress) {
        super(zoneCode, address, extraAddress, detailAddress);
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }
}

