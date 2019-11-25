package com.animalheart.animalheart.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MessagesController {

    @GetMapping("/messages")
    public String index() {
        return "messages";
    }

}
