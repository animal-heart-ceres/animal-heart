package com.animalheart.animalheart.controllers;

import com.animalheart.animalheart.models.*;
import com.animalheart.animalheart.repositories.EventRepository;
import com.animalheart.animalheart.repositories.FollowerRepository;
import com.animalheart.animalheart.repositories.OrganizationProfileRepository;
import com.animalheart.animalheart.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class OrganizationProfileController {

    @Autowired
    OrganizationProfileRepository organizationProfileDao;

    @Autowired
    UserRepository userDao;

    @Autowired
    EventRepository eventDao;

    @Autowired
    FollowerRepository followerDao;

    @GetMapping("/create-organization-profile/{id}")
    public String showCreateOrganizationProfileForm(@PathVariable Long id, Model vModel) {
        vModel.addAttribute("organizationProfile", new OrganizationProfile());
        vModel.addAttribute("organizationId", id);
        return "create-organization-profile";
    }

    @PostMapping("/create-organization-profile")
    public String createOrganizationProfile(@ModelAttribute OrganizationProfile organizationProfile, @RequestParam(name = "organizationId") Long organizationId) {
        User organization = userDao.getOne(organizationId);
        organizationProfile.setOrganization(organization);
        organizationProfileDao.save(organizationProfile);
        return "redirect:/login";
    }

    @GetMapping("/organizations")
    public String viewAllOrganizations(Model vModel){
        List<OrganizationProfile> allProfiles = organizationProfileDao.findAll();
        vModel.addAttribute("organizations", allProfiles);
        return"view-organizations";
    }

    @GetMapping("/organization-profile")
    public String showOrganizationProfile( Model vModel) {
        //When I go to my profile, I expect to see all the animals I have added
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User loggedInUser = userDao.getOne(user.getId());

        OrganizationProfile loggedInOrganizationProfile = organizationProfileDao.findByOrganizationId(user.getId());

        List<Animal> animalList = loggedInUser.getAnimalList();

        List<Event> usersEvents = loggedInUser.getEventList();

        List<Follower> followerList = followerDao.findAll();

        vModel.addAttribute("organizationProfile", loggedInOrganizationProfile);
        vModel.addAttribute("usersEvents", usersEvents);
        vModel.addAttribute("animal", new Animal());
        vModel.addAttribute("event", new Event());
        vModel.addAttribute("animals", animalList);
        vModel.addAttribute("followerList", followerList);

        return "organization-profile";
    }

    @GetMapping("/organization-profile/{id}")
    public String showIndOrganizationProfile(@PathVariable Long id, Model vModel){

        OrganizationProfile currentOrganizationProfile = organizationProfileDao.getOne(id);

        List<Animal> animalList = currentOrganizationProfile.getOrganization().getAnimalList();

        User loggedInUser = currentOrganizationProfile.getOrganization();

        List<Event> usersEvents = loggedInUser.getEventList();

        List<Follower> followerList = followerDao.findAll();

        vModel.addAttribute("usersEvents", usersEvents);
        vModel.addAttribute("animal", new Animal());
        vModel.addAttribute("event", new Event());
        vModel.addAttribute("animals", animalList);
        vModel.addAttribute("organizationProfile", currentOrganizationProfile);
        vModel.addAttribute("followerList", followerList);
        return "organization-profile";
    }


    @PostMapping("/organization-profile/{id}/edit")
    public String editOrganizationProfile(@PathVariable long id, @RequestParam(name = "name") String name, @RequestParam(name = "taxNumber") String taxNumber, @RequestParam(name = "address") String address, @RequestParam(name = "description") String description){
        OrganizationProfile oldProfile = organizationProfileDao.getOne(id);
        oldProfile.setName(name);
        oldProfile.setTaxNumber(Long.parseLong(taxNumber));
        oldProfile.setAddress(address);
        oldProfile.setDescription(description);
        organizationProfileDao.save(oldProfile);
        return "redirect:/organization-profile";
    }


}
