package com.animalheart.animalheart.controllers;

import com.animalheart.animalheart.models.Animal;
import com.animalheart.animalheart.models.OrganizationProfile;
import com.animalheart.animalheart.models.User;
import com.animalheart.animalheart.models.UserProfile;
import com.animalheart.animalheart.repositories.OrganizationProfileRepository;
import com.animalheart.animalheart.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class OrganizationProfileController {

    @Autowired
    OrganizationProfileRepository organizationProfileDao;

    @Autowired
    UserRepository userDao;

    @GetMapping("/create-organization-profile/{id}")
    public String showCreateOrganizationProfileForm(@PathVariable Long id, Model vModel) {
        vModel.addAttribute("organizationProfile", new OrganizationProfile());
        vModel.addAttribute("userId", id);
        return "create-organization-profile";
    }

    @PostMapping("/create-organization-profile")
    public String createOrganizationProfile(@ModelAttribute OrganizationProfile organizationProfile) {
        //set fk
        organizationProfileDao.save(organizationProfile);
        return "redirect:/";
    }

    @GetMapping("/organization-profile/{profileId}")
    public String showOrganizationProfile(@PathVariable long profileId, Model vModel) {
        //When I go to my profile, I expect to see all the animals I have added
        OrganizationProfile loggedInOrganizationProfile = organizationProfileDao.getOne(profileId);

        User loggedInOrganization = loggedInOrganizationProfile.getOrganization();

        List<Animal> animalList = loggedInOrganization.getAnimalList();

        vModel.addAttribute("organizationProfile", loggedInOrganizationProfile);
        vModel.addAttribute("animals", animalList);
        return "organization-profile";
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
