package com.animalheart.animalheart.controllers;

import com.animalheart.animalheart.models.UserProfile;
import com.animalheart.animalheart.repositories.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserProfileController {

    @Autowired
    UserProfileRepository userProfileDao;

    @GetMapping("/create-user-profile")
    public String showCreateUserProfileForm(Model vModel) {
        vModel.addAttribute("userProfile", new UserProfile());
        return "/create-user-profile";
    }

    @PostMapping("/create-user-profile")
    public String createUserProfile(@ModelAttribute UserProfile userProfile) {
        userProfileDao.save(userProfile);
        return "redirect:/";
    }
}
