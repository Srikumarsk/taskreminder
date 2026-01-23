package com.task.taskreminder.controller;

import com.task.taskreminder.model.User;
import com.task.taskreminder.repository.UserRepository;
import jakarta.servlet.http.HttpSession;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ProfileController {

    private final UserRepository userRepository;

    public ProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //  View profile page
@GetMapping("/profile")
public String profile(HttpSession session, Model model) {

    User sessionUser = (User) session.getAttribute("loggedUser");
    if (sessionUser == null) {
        return "redirect:/login";
    }

    // Always fetch fresh user from DB
    User user = userRepository
            .findById(sessionUser.getId())
            .orElse(null);

    model.addAttribute("user", user);

    //  Update session too
    session.setAttribute("loggedUser", user);

    return "profile";
}


    //  Update profile
   @PostMapping("/profile/update")
public String updateProfile(@ModelAttribute User formUser, HttpSession session) {

    User sessionUser = (User) session.getAttribute("loggedUser");
    if (sessionUser == null) {
        return "redirect:/login";
    }

    // 🔥 Fetch ORIGINAL user from DB
    User user = userRepository.findById(sessionUser.getId()).orElse(null);

    if (user != null) {
        user.setName(formUser.getName());
        // ⚠️ DO NOT touch profileImage
        userRepository.save(user);

        // Update session
        session.setAttribute("loggedUser", user);
    }

    return "redirect:/profile";
}

@PostMapping("/profile/upload")
public String uploadProfilePhoto(
        @RequestParam("photo") MultipartFile photo,
        HttpSession session) throws IOException {

    User sessionUser = (User) session.getAttribute("loggedUser");
    if (sessionUser == null) {
        return "redirect:/login";
    }
String uploadDir = "C:/taskreminder_uploads/profile/";

    File dir = new File(uploadDir);
    if (!dir.exists()) dir.mkdirs();

    String fileName = sessionUser.getId() + "_" + photo.getOriginalFilename();
    File dest = new File(uploadDir + fileName);
    photo.transferTo(dest);

    // 🔥 Update DB
    sessionUser.setProfileImage(fileName);
    userRepository.save(sessionUser);

    // 🔥 Update session
    session.setAttribute("loggedUser", sessionUser);

    return "redirect:/profile";
}

}
