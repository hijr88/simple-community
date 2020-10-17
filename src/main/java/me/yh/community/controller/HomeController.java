package me.yh.community.controller;

import me.yh.community.Utils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.security.Principal;

@Controller
public class HomeController {

    @GetMapping({"", "/"})
    public String index() {
        return "index";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/pra")
    @ResponseBody
    public Principal principal(Principal principal) {
        return principal;
    }

    @GetMapping("/error-redirect")
    public String error(Model model) {
        String type = (String) model.getAttribute("type");
        if (type == null) {
            Utils.redirectErrorPage(model, "시작 페이지로 이동합니다.", "/index");
            return "error-redirect";
        }
        switch (type) {
            case "FAIL_ADD_MEMBER":
                Utils.redirectErrorPage(model, "회원정보 추가에 실패하였습니다.\\n아이디나 이메일이 중복입니다.", "/index");break;
            case "FAIL_BINDING":
                Utils.redirectErrorPage(model, "잘못된 값이 입력 되었습니다.", "/index");break;
        }
        return "error-redirect";
    }
}
