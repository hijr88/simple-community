package me.yh.community.security;

import me.yh.community.config.security.CustomUser;
import me.yh.community.entity.Member;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements
        WithSecurityContextFactory<WithMockCustomUser> {

    public SecurityContext createSecurityContext(WithMockCustomUser withUser) {

        if (withUser.role().startsWith("ROLE_")) {
            throw new IllegalArgumentException("roles cannot start with ROLE_ Got "
                    + withUser.role());
        }

        Member member = Member.testUser(withUser.username(), "ROLE_" + withUser.role());

        CustomUser principal = new CustomUser(member);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                principal, principal.getPassword(), principal.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        return context;
    }
}