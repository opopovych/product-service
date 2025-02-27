package com.example.productservice.controlers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login() {
        return "login"; // Ваша сторінка логіну (наприклад, login.html у папці templates)
    }

    @GetMapping("/")
    public String home() {
        return "home"; // Ваша домашня сторінка (home.html у templates)
    }
}
