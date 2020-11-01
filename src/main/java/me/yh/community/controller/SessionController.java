package me.yh.community.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SessionController {

    private final SessionRegistry sessionRegistry;

    @GetMapping("/all")
    public List<Object> all() {
        List<Object> allPrincipals = sessionRegistry.getAllPrincipals();
        System.out.println("현재 인증된 사용자수: " + allPrincipals.size());

        allPrincipals.forEach(o -> {
            if(o instanceof User) {
                UserDetails userDetails = (UserDetails) o;
                System.out.println("username: " + userDetails.getUsername());
            }
        });

        return allPrincipals;
    }
}
