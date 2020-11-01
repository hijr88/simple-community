package me.yh.community.controller.admin;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin")
@Controller
public class AdminHomeController {

    /**
     * @return 관리자 페이지
     */
    @GetMapping(path = {"/index", ""})
    public String  index() {

        return "admin/index";
    }
}
