package me.yh.community.config.security;

import me.yh.community.Utils;
import me.yh.community.entity.Member;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public class CustomUser extends User {

    private String profileImage;

    public CustomUser(Member member) {
        super(member.getId(), member.getPassword(), member.getEnable(), true, true, true,
                List.of(new SimpleGrantedAuthority(member.getRole())));
        this.profileImage = Utils.urlEncode(member.getProfileImage());
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
