package com.chatgpt.controller;

import com.chatgpt.model.UserInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {


    @GetMapping("/main")
    public String mainPage(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            // 세션이 없으면 로그인 페이지로 리다이렉션
            return "redirect:/loginPage";
        }
        // 세션이 있으면 메인 페이지로 리다이렉션
        return "redirect:/main.html";
    }


    @GetMapping("/loginPage")
    public String login() {
        return "loginPage.html";
    }


    @GetMapping("/chat")
    public String chatPage(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            return "redirect:/loginPage";
        }
        return "redirect:/chat.html";
    }


    @GetMapping("/chatLog")
    public String chatLogPage(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            return "redirect:/loginPage";
        }
        return "redirect:/chatLog.html";
    }


    @GetMapping("/upload")
    public String upload() {
        return "upload.html";
    }


    //
    @GetMapping("/signup")
    public String signupPage() {
        return "signup.html"; // signup.html 파일의 이름
    }

    @GetMapping("/adminPage")
    public String adminPage(){return "adminPage.html";}


}