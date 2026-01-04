package com.task.taskreminder.service;

import com.task.taskreminder.model.User;
import com.task.taskreminder.repository.UserRepository;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;

    public UserService(UserRepository userRepository,
                       EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    // REGISTER
    public String register(User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            return "EMAIL_EXISTS";
        }

        String otp = String.valueOf((int)(Math.random() * 900000) + 100000);

        user.setOtp(otp);
        user.setVerified(false);

        userRepository.save(user);
        emailService.sendOTP(user.getEmail(), otp);

        System.out.println("Generated OTP: " + otp);

        return "OTP_SENT";
    }

    // VERIFY OTP
    public boolean verifyOtp(String email, String otp) {
        User user = userRepository.findByEmail(email);

        if (user != null && otp.equals(user.getOtp())) {
            user.setVerified(true);
            user.setOtp(null);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    // LOGIN
    public User login(String email, String password) {
        User user = userRepository.findByEmail(email);

        if (user == null) return null;
        if (!user.isVerified()) return null;

        if (user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
    public void deleteUser(Integer id) {
    userRepository.deleteById(id);
}
@GetMapping("/users")
public String usersPage(Model model) {
    model.addAttribute("users", userRepository.findAll());
    return "users";
}

public @Nullable Object getAllUsers() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getAllUsers'");
}

}