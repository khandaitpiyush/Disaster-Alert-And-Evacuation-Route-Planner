package com.example.disaster_management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/login")
    public String showLogin() {
        return "login"; // ✅ handles /login properly
    }

    @GetMapping("/user-dashboard")
    public String showUserDashboard() {
        return "user-dashboard";
    }

    @GetMapping("/admin-dashboard")
    public String showAdminDashboard() {
        return "admin-dashboard";
    }

    @GetMapping("/support")
    public String showSupportPage() {
        return "support";
    }

    @GetMapping("/news-alerts")
    public String showNewsAlertsPage() {
        return "news-alerts";
    }
}
