package com.animalheart.animalheart.controllers;

import com.animalheart.animalheart.models.UserProfile;
import com.animalheart.animalheart.repositories.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/user-profile/{id}")
    public String showUserProfile(@PathVariable long id, Model vModel) {
        vModel.addAttribute("userProfile", userProfileDao.findById(id));
        return "/index";
    }

    @PostMapping("/profile/{id}/edit")
    public String editUserProfile(@PathVariable long id){

    }

//    @PostMapping("/posts/{id}/edit")
//    public String editPost(@PathVariable long id, @RequestParam(name = "title") String title, @RequestParam(name = "body") String body){
//        Post oldPost = postDao.getOne(id);
//        oldPost.setTitle(title);
//        oldPost.setBody(body);
//        postDao.save(oldPost);
//        return "redirect:/posts";
//    }
}
