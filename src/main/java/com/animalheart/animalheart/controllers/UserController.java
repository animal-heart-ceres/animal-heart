package com.animalheart.animalheart.controllers;

import com.animalheart.animalheart.models.Follower;
import com.animalheart.animalheart.models.User;
import com.animalheart.animalheart.repositories.FollowerRepository;
import com.animalheart.animalheart.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    UserRepository userDao;

    @Autowired
    FollowerRepository followerDao;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

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

    @PostMapping("/follow")
    public String createFollower(@RequestParam(name = "followerId") Long followerId) {
        //set the fk to the id of the organization

        Follower newFollower = new Follower();
        newFollower.setFollowerId(followerId);
        followerDao.save(newFollower);

        return "redirect:/";
    }

    @PostMapping("/delete-follow/{userId}/{orgId}")
    public String deleteFollower(@PathVariable Long userId, @PathVariable Long orgId) {

        List<Follower> followerList = followerDao.findAll();
        for(Follower follower : followerList){
            if(follower.getFollowerId() == userId && follower.getUser().getId() == orgId) {
                followerDao.delete(follower);
            }
        }
        return "redirect:/";
    }

}
