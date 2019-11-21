package com.animalheart.animalheart.controllers;

import com.animalheart.animalheart.models.Animal;
import com.animalheart.animalheart.repositories.AnimalRepository;
import com.animalheart.animalheart.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestJSONController {

    @Autowired
    AnimalRepository animalDao;

    @GetMapping("/animals.json")
    public @ResponseBody List<Animal> viewAllAnimalsInJSONFormat() {
        return animalDao.findAll();
    }


}
