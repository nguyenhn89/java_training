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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
        model.addAttribute("login", new AuthRequest());
        return "sign-in";
    }

    @GetMapping("/sign-up")
    public String showSignUpForm(Model model) {
        model.addAttribute("register", new RegisterDTO());
        return "sign-up";
    }


    // ----- Xử lý form đăng ký -----
    @PostMapping("/sign-up")
    public String processSignUp(
            @Valid @ModelAttribute("register") RegisterDTO registerDTO,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            System.out.println(1);
            return "sign-up";
        }

        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            System.out.println(2);
            model.addAttribute("passwordError", "Mật khẩu xác nhận không khớp");
            return "sign-up";
        }

        if (userService.existsByUserName(registerDTO.getUserName())) {
            System.out.println(3);
            model.addAttribute("usernameError", "Tên đăng nhập đã tồn tại");
            return "sign-up";
        }

        try {
            User user = new User();
            user.setUserName(registerDTO.getUserName());
            user.setPassword(registerDTO.getPassword());
            user.setEmail(registerDTO.getEmail());
            userService.save(user);
        } catch (RuntimeException ex) {
            model.addAttribute("usernameError", ex.getMessage());
            System.out.println(ex.getMessage());
            return "sign-up";
        }

        return "redirect:/sign-in?registered";
    }

    @PostMapping("/sign-in")
    public String processLogin(@ModelAttribute("login") AuthRequest authRequest,
                               HttpServletRequest request,
                               Model model) {
        try {
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
            model.addAttribute("loginError", "Sai tên đăng nhập hoặc mật khẩu");
            return "sign-in";
        }
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {

        // Lấy thông tin xác thực hiện tại
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Object principal = authentication.getPrincipal();
//
//        String username = null;
//        String role = null;
//
//        if (principal instanceof UserDetails userDetails) {
//            username = userDetails.getUsername();
//            role = userDetails.getAuthorities().stream()
//                    .map(a -> a.getAuthority())
//                    .findFirst()
//                    .orElse("UNKNOWN");
//        } else {
//            username = principal.toString();
//        }
//
//        model.addAttribute("username", username);
//        System.out.println("username : " + username);
//        model.addAttribute("role", role);
        model.addAttribute("currentPageSidebar", "dashboard");

        return "dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextHolder.clearContext();

        // Hủy session hiện tại (xoá luôn SPRING_SECURITY_CONTEXT)
        request.getSession().invalidate();

        return "redirect:/sign-in?logout";
    }
}
