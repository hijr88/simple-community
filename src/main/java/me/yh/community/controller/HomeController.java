package me.yh.community.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
public class HomeController {

    @GetMapping({"","/"})
    public String index() {
        return "index";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/pra")
    @ResponseBody
    public Principal principal(Principal principal) {
        return principal;
    }
}
