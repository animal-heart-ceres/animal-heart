package com.animalheart.animalheart.controllers;

import com.animalheart.animalheart.models.*;
import com.animalheart.animalheart.repositories.EventRepository;
import com.animalheart.animalheart.repositories.FollowerRepository;
import com.animalheart.animalheart.repositories.UserProfileRepository;
import com.animalheart.animalheart.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserProfileController {

    @Autowired
    UserProfileRepository userProfileDao;

    @Autowired
    UserRepository userDao;

    @Autowired
    FollowerRepository followerDao;

    @Autowired
    EventRepository eventDao;

    @GetMapping("/create-user-profile/{id}")
    public String showCreateUserProfileForm(@PathVariable Long id, Model vModel) {
        vModel.addAttribute("userProfile", new UserProfile());
        vModel.addAttribute("userId", id);
        return "create-user-profile";
    }

    @PostMapping("/create-user-profile")
    public String createUserProfile(@ModelAttribute UserProfile userProfile, @RequestParam(name = "userId") Long userId) {
        User currentUser = userDao.getOne(userId);
        userProfile.setUser(currentUser);
        userProfileDao.save(userProfile);
        return "redirect:/login";
    }

    @GetMapping("/user-profile")
    public String showUserProfile(Model vModel) {
        //When I go to my profile, I expect to see all the animals I have added
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User loggedInOrganization = userDao.getOne(user.getId());
        if(loggedInOrganization.getOrganization()) {
            return "redirect:/organization-profile";
        } else {
            UserProfile loggedInUserProfile = userProfileDao.findByUserId(user.getId());

            User loggedInUser = loggedInUserProfile.getUser();

            List<Animal> animalList = loggedInUser.getAnimalList();
            List<Follower> followerList = followerDao.findAll();
            List<Event> followerEvents = new ArrayList<>();

            for(Follower follow : followerList){
                List<Event> eventList1 = follow.getUser().getEventList();
                if(follow.getFollowerId() == loggedInUser.getId())
                followerEvents.addAll(eventList1);
            }


            vModel.addAttribute("userProfile", loggedInUserProfile);
            vModel.addAttribute("animals", animalList);
            vModel.addAttribute("animal", new Animal());
            vModel.addAttribute("events", followerEvents);
            vModel.addAttribute("follows", followerList);

            return "user-profile";
        }
    }

    @GetMapping("/user-profile/{profileId}")
    public String showIndUserProfile(Model vModel, @PathVariable Long profileId) {
        //When I go to my profile, I expect to see all the animals I have added

            UserProfile loggedInUserProfile = userProfileDao.getOne(profileId);

            User loggedInUser = loggedInUserProfile.getUser();

            List<Animal> animalList = loggedInUser.getAnimalList();
            List<Follower> followerList = followerDao.findAll();
            List<Event> followerEvents = new ArrayList<>();

            for(Follower follow : followerList){
                List<Event> eventList1 = follow.getUser().getEventList();
                if(follow.getFollowerId() == loggedInUser.getId())
                    followerEvents.addAll(eventList1);
            }


            vModel.addAttribute("userProfile", loggedInUserProfile);
            vModel.addAttribute("animals", animalList);
            vModel.addAttribute("animal", new Animal());
            vModel.addAttribute("events", followerEvents);
            vModel.addAttribute("follows", followerList);

            return "user-profile";

    }

    @PostMapping("/profile/{profileId}/edit")
    public String editUserProfile(@PathVariable long profileId, @RequestParam(name = "firstName") String firstName, @RequestParam(name = "lastName") String lastName, @RequestParam(name = "address") String address){
        UserProfile oldProfile = userProfileDao.getOne(profileId);
        oldProfile.setFirstName(firstName);
        oldProfile.setLastName(lastName);
        oldProfile.setAddress(address);
        userProfileDao.save(oldProfile);
        return "redirect:/user-profile";
    }

}
