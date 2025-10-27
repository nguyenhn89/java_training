package org.example.java_training.controller.web;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute
    public void addUserInfo(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            String username = null;
            String role = null;
            if (principal instanceof UserDetails userDetails) {
                username = userDetails.getUsername();
                role = userDetails.getAuthorities().stream()
                        .map(a -> a.getAuthority())
                        .findFirst()
                        .orElse("UNKNOWN");
            } else {
                username = principal.toString();
            }

            model.addAttribute("username", username);
            model.addAttribute("role", role);
        }
    }
}
