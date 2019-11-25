package com.animalheart.animalheart.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutUsController {

    @GetMapping("/about-us")
    public String showAboutUs() {
        return "about-us";
    }
}
