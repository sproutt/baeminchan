package codesquad.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/login.html")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/member/join")
    public String form() {
        return "join";
    }

    @GetMapping("/member/login")
    public String loginForm2() {
        return "login";
    }

}
