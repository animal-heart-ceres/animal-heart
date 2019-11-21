package com.animalheart.animalheart.controllers;

import com.animalheart.animalheart.models.OrganizationProfile;
import com.animalheart.animalheart.models.UserProfile;
import com.animalheart.animalheart.repositories.OrganizationProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class OrganizationProfileController {

    @Autowired
    OrganizationProfileRepository organizationProfileDao;

    @GetMapping("/organization-sign-up")
    public String showCreateUserProfileForm(Model vModel) {
        vModel.addAttribute("organizationProfile", new OrganizationProfile());
        return "/organization-sign-up";
    }

    @PostMapping("/organization-sign-up")
    public String createUserProfile(@ModelAttribute OrganizationProfile organizationProfile) {
        organizationProfileDao.save(organizationProfile);
        return "redirect:/";
    }

//    @PostMapping("/organization-profile/{id}/edit")
//    public String editUserProfile(@PathVariable long id, @RequestParam(name = "name") String name, @RequestParam(name = "lastName") String lastName, @RequestParam(name = "address") String address){
//        OrganizationProfile oldProfile = organizationProfileDao.getOne(id);
//        oldProfile.setName(firstName);
//        oldProfile.setLastName(lastName);
//        oldProfile.setAddress(address);
//        userProfileDao.save(oldProfile);
//        return "redirect:/";
//    }

}
