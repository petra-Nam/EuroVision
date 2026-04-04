package com.example.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/login")
    public String showLoginPage() {
        // Spring looks in src/main/resources/templates for "login.html"
        return "login";
    }

    @GetMapping("/songs")
    public String showSongsPage() {
        // Spring looks in src/main/resources/templates for "songs.html"
        return "songs";
    }
}