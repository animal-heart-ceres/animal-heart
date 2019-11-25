package com.animalheart.animalheart.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MapController {

    @GetMapping("/map")
    public String index() {
        return "map";
    }

}
