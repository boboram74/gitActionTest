package core.ghayoun.mygitai.front.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FrontController {

    @GetMapping("/")
    public String home (HttpServletRequest request) {
        if (request.getSession().getAttribute("user") == null) {
            return "redirect:member/login";
        }
        return "index";
    }

    @GetMapping("/member/login")
    public String getLogin (HttpServletRequest request) {
        return "member/login";
    }

    @PostMapping("/member/login")
    public String postLogin (HttpServletRequest request, HttpSession session) {
        if (request.getParameter("id").equals("admin") && request.getParameter("password").equals("password")) {
            session.setAttribute("user","어드민");
        }
        return "redirect:/";
    }

    @GetMapping("/member/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}