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
    public String showCreateOrganizationProfileForm(Model vModel) {
        vModel.addAttribute("organizationProfile", new OrganizationProfile());
        return "/organization-sign-up";
    }

    @PostMapping("/organization-sign-up")
    public String createOrganizationProfile(@ModelAttribute OrganizationProfile organizationProfile) {
        organizationProfileDao.save(organizationProfile);
        return "redirect:/";
    }

    @PostMapping("/organization-profile/{id}/edit")
    public String editOrganizationProfile(@PathVariable long id, @RequestParam(name = "name") String name, @RequestParam(name = "taxNumber") Long taxNumber, @RequestParam(name = "address") String address, @RequestParam(name = "description") String description){
        OrganizationProfile oldProfile = organizationProfileDao.getOne(id);
        oldProfile.setName(name);
        oldProfile.setTaxNumber(taxNumber);
        oldProfile.setAddress(address);
        oldProfile.setDescription(description);
        organizationProfileDao.save(oldProfile);
        return "redirect:/";
    }

}
