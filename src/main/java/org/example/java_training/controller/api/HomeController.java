package org.example.java_training.controller.api;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("author", "NguyenHN");
        model.addAttribute("message", "OK");
        return "home"; // Spring Boot sẽ tìm file templates/home.html
    }
}