package me.yh.community.config.security;

import me.yh.community.entity.Member;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public class CustomUser extends User {

    private Member member;

    public CustomUser(Member member) {
        super(member.getUsername(), member.getPassword(), member.getEnable(), true, true, true,
                List.of(new SimpleGrantedAuthority(member.getRole())));
        this.member = member;
    }

    public Member getMember() {
        return member;
    }
}
