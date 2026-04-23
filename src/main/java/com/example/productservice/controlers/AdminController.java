package com.example.productservice.controlers;

import com.example.productservice.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    // Сторінка зі списком користувачів
    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "admin-users";
    }

    // Активація/Деактивація
    @PostMapping("/users/toggle/{id}")
    public String toggleUser(@PathVariable Long id, @RequestParam boolean status) {
        userService.updateStatus(id, status);
        return "redirect:/admin/users";
    }
}