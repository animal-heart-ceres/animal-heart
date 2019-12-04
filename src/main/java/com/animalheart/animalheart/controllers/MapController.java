package com.animalheart.animalheart.controllers;

import com.animalheart.animalheart.models.Animal;
import com.animalheart.animalheart.models.OrganizationProfile;
import com.animalheart.animalheart.models.User;
import com.animalheart.animalheart.models.UserProfile;
import com.animalheart.animalheart.repositories.AnimalRepository;
import com.animalheart.animalheart.repositories.OrganizationProfileRepository;
import com.animalheart.animalheart.repositories.UserProfileRepository;
import com.animalheart.animalheart.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MapController {

    @Autowired
    UserProfileRepository userProfileDao;

    @Autowired
    OrganizationProfileRepository organizationProfileDao;

    @Autowired
    UserRepository userDao;

    @Autowired
    AnimalRepository animalDao;


    @GetMapping("/map")
    public String index(Model vModel) {
        List<UserProfile> userProfiles = userProfileDao.findAll();
        List<OrganizationProfile> organizationProfiles = organizationProfileDao.findAll();
        List<User> users = userDao.findAll();
        List<String> addresses = new ArrayList<>();
        List<Animal> animals = animalDao.findAll();



        for(UserProfile profile : userProfiles) {
            addresses.add(profile.getAddress());
        }

        for(OrganizationProfile profile : organizationProfiles) {
            addresses.add(profile.getAddress());
        }

        vModel.addAttribute("animals", animals);
        vModel.addAttribute("users",users);
        vModel.addAttribute("profiles", addresses);
        vModel.addAttribute("userProfiles", userProfileDao.findAll());
        vModel.addAttribute("organizationProfiles", organizationProfileDao.findAll());
        return "map";
    }

//    @GetMapping("/animals.json")
//    @ResponseBody
//    public List<Animal> jsonAnimals(){
//        return animalDao.findAll();
//    }
//
//    @GetMapping("/user-profiles.json")
//    @ResponseBody
//    public List<UserProfile> jsonUserProfiles(){
//        return userProfileDao.findAll();
//    }
//
//    @GetMapping("/org-profiles.json")
//    @ResponseBody
//    public List<OrganizationProfile> jsonOrgProfiles(){
//        return organizationProfileDao.findAll();
//    }

}
