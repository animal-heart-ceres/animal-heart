package com.animalheart.animalheart.controllers;

import com.animalheart.animalheart.models.Follower;
import com.animalheart.animalheart.models.User;
import com.animalheart.animalheart.repositories.FollowerRepository;
import com.animalheart.animalheart.repositories.UserRepository;
import com.animalheart.animalheart.repositories.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    private Users users;
    private PasswordEncoder passwordEncoder;

    public UserController(Users users, PasswordEncoder passwordEncoder) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    UserRepository userDao;

    @Autowired
    FollowerRepository followerDao;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user.getOrganization()) {
            return "redirect:/create-organization-profile/" + user.getId();
        } else {
            return "redirect:/create-user-profile/" + user.getId();
        }
    }

    @GetMapping("/sign-up")
    public String showSignupForm(Model model){
        model.addAttribute("user", new User());
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String createUser(@ModelAttribute User user, @RequestParam(name = "isOrganization") boolean isOrganization){
        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);
        user.setAdmin(false);
        user.setOrganization(isOrganization);
        userDao.save(user);
        return "redirect:/login";

    }

    @PostMapping("/follower/{orgId}")
    public String createFollower(@PathVariable Long orgId, @RequestParam(name = "followerId") Long followerId) {

        User organization = userDao.getOne(orgId);

        Follower createdFollower = new Follower();
        createdFollower.setFollowerId(followerId);
        createdFollower.setUser(organization);
        followerDao.save(createdFollower);

        return "redirect:/organization-profile/" + orgId;
    }

    @PostMapping("/follower/{userId}/{orgId}/delete")
    public String deleteFollower(@PathVariable Long userId, @PathVariable Long orgId) {

        List<Follower> followerList = followerDao.findAll();
        for(Follower follower : followerList){
            if(follower.getFollowerId() == userId && follower.getUser().getId() == orgId) {
                followerDao.delete(follower);
            }
        }
        return "redirect:/organization-profile/" + orgId;
    }


}
