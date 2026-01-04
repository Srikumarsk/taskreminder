package com.task.taskreminder.controller;

import com.task.taskreminder.model.User;
import com.task.taskreminder.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserService userService;
   
    
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // REGISTER PAGE
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("showOtp", false);
        return "register";
    }
    @GetMapping("/")
public String rootRedirect() {
    return "redirect:/login";
}

    @GetMapping("/login")
public String loginPage() {
    return "login";
}
 @GetMapping("/users")
public String userspage() {
    return "users";
}
@PostMapping("/login")
public String login(@RequestParam String email,
                    @RequestParam String password,
                    HttpSession session,
                    Model model) {

    User user = userService.login(email, password);

    if (user == null) {
        model.addAttribute("error", "Invalid credentials or not verified");
        return "login";
    }

    session.setAttribute("loggedUser", user);
    return "redirect:/home";
}



    // SEND OTP
    @PostMapping("/send-otp")
    public String sendOtp(@ModelAttribute User user, Model model) {

        String result = userService.register(user);

        if (result.equals("EMAIL_EXISTS")) {
            model.addAttribute("error", "Email already exists");
            model.addAttribute("showOtp", false);
            return "register";
        }

        model.addAttribute("email", user.getEmail());
        model.addAttribute("showOtp", true);
        model.addAttribute("message", "OTP sent to your email");
        return "register";
    }

    // VERIFY OTP
    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String email,
                            @RequestParam String otp,
                            Model model) {

        boolean verified = userService.verifyOtp(email, otp);

        if (verified) {
            return "redirect:/login";
        }

        model.addAttribute("email", email);
        model.addAttribute("showOtp", true);
        model.addAttribute("error", "Invalid OTP");
        return "register";
    }
    @GetMapping("/delete-user/{id}")
public String deleteUser(@PathVariable Integer id) {
    userService.deleteUser(id);
    return "redirect:/users";
}




}
