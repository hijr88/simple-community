package me.yh.community.controller;

import me.yh.community.Utils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
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
            return "error/error-redirect";
        }
        switch (type) {
            case "FAIL_ADD_MEMBER":
                Utils.redirectErrorPage(model, "회원정보 추가에 실패하였습니다.\\n아이디나 이메일이 중복입니다.", "/index");break;
            case "FAIL_BINDING":
                Utils.redirectErrorPage(model, "잘못된 값이 입력 되었습니다.", "/index");break;
            case "FAIL_MODIFY_MEMBER":
                Utils.redirectErrorPage(model, "회원 수정을 실패하였습니다.","/members/me");break;
        }
        return "error/error-redirect";
    }

    @GetMapping(path = "/expired")
    public void expired(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String root = request.getContextPath();

        response.setContentType("text/html;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        String msg = "<script>" +
                "        alert('다른 사용자가 접속하여 로그인이 해제되었습니다.');" +
                "        location.href= ' " + root + "/index';" +
                "    </script>";
        out.write(msg);
    }

    @GetMapping("/denied")
    public String denied() {
        return "error/403";
    }
}
