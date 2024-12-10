package com.example.jungleboard.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ViewController {

    @GetMapping("/")
    public String home() {
        return "redirect:/posts";
    }

    // Auth related pages
    @GetMapping("/auth/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/auth/signup")
    public String signupPage() {
        return "auth/signup";
    }

    // Post related pages
    @GetMapping("/posts")
    public String postList() {
        return "posts/list";
    }

    @GetMapping("/posts/{postId}")
    public String postDetail(@PathVariable Long postId) {
        return "posts/detail";
    }

    @GetMapping("/posts/write")
    public String postWritePage() {
        return "posts/write";
    }

    @GetMapping("/posts/{postId}/edit")
    public String postEditPage(@PathVariable Long postId) {
        return "posts/edit";
    }

    // MyPage
    @GetMapping("/mypage")
    public String myPage() {
        return "user/mypage";
    }

    // OAuth2 redirect
    @GetMapping("/oauth2/redirect")
    public String oauth2RedirectHandler() {
        return "auth/oauth2-redirect";
    }
}