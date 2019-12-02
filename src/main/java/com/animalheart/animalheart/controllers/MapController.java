package com.animalheart.animalheart.controllers;

import com.animalheart.animalheart.models.UserProfile;
import com.animalheart.animalheart.repositories.OrganizationProfileRepository;
import com.animalheart.animalheart.repositories.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MapController {

    @Autowired
    UserProfileRepository userProfileDao;

    @Autowired
    OrganizationProfileRepository organizationProfileDao;


    @GetMapping("/map")
    public String index(Model vModel) {
        vModel.addAttribute("userProfiles", userProfileDao.findAll());
        vModel.addAttribute("organizationProfiles", organizationProfileDao.findAll());
        return "map";
    }


}
