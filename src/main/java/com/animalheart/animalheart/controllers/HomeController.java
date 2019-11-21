package com.animalheart.animalheart.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/animals/ajax")
    public String viewAllAdsWithAjax() {
        return "Json";
    }

}
