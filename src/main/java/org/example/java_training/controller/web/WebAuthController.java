package org.example.java_training.controller.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.example.java_training.domain.User;
import org.example.java_training.dto.RegisterDTO;
import org.example.java_training.request.AuthRequest;
import org.example.java_training.security.JwtUtil;
import org.example.java_training.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class WebAuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public WebAuthController(AuthenticationManager authenticationManager, UserService userService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/sign-in")
    public String showLoginForm(Model model) {
        if (!model.containsAttribute("login")) {
            model.addAttribute("login", new AuthRequest());
        }
        return "sign-in";
    }

    @GetMapping("/sign-up")
    public String showSignUpForm(Model model) {
        if (!model.containsAttribute("register")) {
            model.addAttribute("register", new RegisterDTO());
        }
        return "sign-up";
    }


    @PostMapping("/sign-up")
    public String processSignUp(
            @Valid @ModelAttribute("register") RegisterDTO registerDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.register", result);
            redirectAttributes.addFlashAttribute("register", registerDTO);
            return "redirect:/sign-up";
        }

        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.register", "Confirmation password does not match");
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.register", result);
            redirectAttributes.addFlashAttribute("register", registerDTO);
            return "redirect:/sign-up";
        }

        if (userService.existsByUserName(registerDTO.getUserName())) {
            result.rejectValue("userName", "error.register", "The username already exists.");
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.register", result);
            redirectAttributes.addFlashAttribute("register", registerDTO);
            return "redirect:/sign-up";
        }

        if (userService.existsByEmail(registerDTO.getEmail())) {
            result.rejectValue("email", "error.register", "The email already exists.");
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.register", result);
            redirectAttributes.addFlashAttribute("register", registerDTO);
            return "redirect:/sign-up";
        }

        try {
            User user = new User();
            user.setUserName(registerDTO.getUserName());
            user.setPassword(registerDTO.getPassword());
            user.setEmail(registerDTO.getEmail());
            userService.save(user);
        } catch (RuntimeException ex) {
            result.rejectValue("userName", "error.register", ex.getMessage());
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.register", result);
            redirectAttributes.addFlashAttribute("register", registerDTO);
            return "redirect:/sign-up";
        }

        return "redirect:/sign-in?registered";
    }

    @PostMapping("/sign-in")
    public String processLogin(@ModelAttribute("login") AuthRequest authRequest,
                               HttpServletRequest request,
                               RedirectAttributes redirectAttributes) {
        try {
            String username = authRequest.getUserName();
            String password = authRequest.getPassword();

            if (username == null || username.trim().isEmpty() ||
                    password == null || password.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("loginError", "Please enter your username or password");
                return "redirect:/sign-in";
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUserName(),
                            authRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

            return "redirect:/dashboard";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("loginError", "Incorrect username or password");
            return "redirect:/sign-in";
        }
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        model.addAttribute("currentPageSidebar", "dashboard");

        return "dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextHolder.clearContext();

        request.getSession().invalidate();

        return "redirect:/sign-in?logout";
    }
}
