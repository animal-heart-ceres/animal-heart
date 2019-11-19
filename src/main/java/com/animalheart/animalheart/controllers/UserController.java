package com.animalheart.animalheart.controllers;

import com.animalheart.animalheart.models.User;
import com.animalheart.animalheart.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    UserRepository userDao;

    @GetMapping("/sign-up")
    public String showSignupForm(Model model){
        model.addAttribute("user", new User());
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String createUser(@ModelAttribute User user){
        user.setAdmin(false);
        user.setOrganization(false);
        userDao.save(user);
        return "redirect:/";
    }

    @GetMapping("/sign-up/organization")
    public String showOrganizationSignUpForm(Model model){
        model.addAttribute("user", new User());
        return "sign-up";
    }

    @PostMapping("/sign-up/organization")
    public String createOrganization(@ModelAttribute User user){
        user.setAdmin(false);
        user.setOrganization(true);
        userDao.save(user);
        return "redirect:/";
    }
}
