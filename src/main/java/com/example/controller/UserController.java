package com.example.controller;

import com.example.model.UserStatus;
import com.example.service.UserService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;

@Controller
public class UserController {

    private final UserService userService;
    private final SimpMessagingTemplate template;

    public UserController(UserService userService, SimpMessagingTemplate template) {
        this.userService = userService;
        this.template = template;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // login.html
    }

    @PostMapping("/home")
    public String homePage(String username, Model model, HttpSession session) {
        boolean added = userService.addUser(username);
        if (!added) {
            model.addAttribute("error", "Username already in use!");
            return "login";
        }

        session.setAttribute("username", username);
        model.addAttribute("username", username);

        template.convertAndSend("/topic/status", userService.getAllUsers());
        return "home"; // home.html
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        String username = (String) session.getAttribute("username");
        if(username != null) {
            userService.removeUser(username);
            template.convertAndSend("/topic/status", userService.getAllUsers());
            session.invalidate();
        }
        return "redirect:/login?logout=true";
    }
    @GetMapping("/current-users")
    @ResponseBody
    public Collection<UserStatus> getAllUsers() {
        return userService.getAllUsers();
    }
}
