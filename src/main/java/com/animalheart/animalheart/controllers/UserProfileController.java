package com.animalheart.animalheart.controllers;

import com.animalheart.animalheart.models.User;
import com.animalheart.animalheart.models.UserProfile;
import com.animalheart.animalheart.repositories.UserProfileRepository;
import com.animalheart.animalheart.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserProfileController {

    @Autowired
    UserProfileRepository userProfileDao;

    @Autowired
    UserRepository userDao;

    @GetMapping("/create-user-profile")
    public String showCreateUserProfileForm(Model vModel) {
        vModel.addAttribute("userProfile", new UserProfile());
        return "/create-user-profile";
    }

    @PostMapping("/create-user-profile")
    public String createUserProfile(@ModelAttribute UserProfile userProfile) {
        userProfile.setUser(userDao.getOne(14L));
        userProfileDao.save(userProfile);
        return "redirect:/";
    }

    @GetMapping("/user-profile/{id}")
    public String showUserProfile(@PathVariable long id, Model vModel) {
        vModel.addAttribute("userProfile", userProfileDao.findById(id));
        return "/index";
    }

    @PostMapping("/profile/{id}/edit")
    public String editUserProfile(@PathVariable long id, @RequestParam(name = "firstName") String firstName, @RequestParam(name = "lastName") String lastName, @RequestParam(name = "address") String address){
        UserProfile oldProfile = userProfileDao.getOne(id);
        oldProfile.setFirstName(firstName);
        oldProfile.setLastName(lastName);
        oldProfile.setAddress(address);
        userProfileDao.save(oldProfile);
        return "redirect:/";
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
