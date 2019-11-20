package com.animalheart.animalheart.controllers;

import com.animalheart.animalheart.models.OrganizationProfile;
import com.animalheart.animalheart.models.UserProfile;
import com.animalheart.animalheart.repositories.OrganizationProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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

}
