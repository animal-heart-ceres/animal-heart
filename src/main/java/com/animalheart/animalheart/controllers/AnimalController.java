package com.animalheart.animalheart.controllers;

import com.animalheart.animalheart.models.Animal;
import com.animalheart.animalheart.repositories.AnimalRepository;
import com.animalheart.animalheart.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AnimalController {

    @Autowired
    AnimalRepository animalDao;

    @Autowired
    UserRepository userDao;

    @GetMapping("/create-animal")
    public String showAnimalForm(Model vModel){
        vModel.addAttribute("animal", new Animal());
        return"/index";
    }

    @PostMapping("/create-animal")
    public String createAnimal(@ModelAttribute Animal animal){
//        set fk
        animalDao.save(animal);
        return "redirect:/";
    }

    @GetMapping("/animals")
    public String showAllAnimals(Model vModel) {
        List<Animal> animalList = animalDao.findAll();
        vModel.addAttribute("animalList", animalList);
        return "/index";
    }

    @GetMapping("/animal/{id}")
    public String showAnimal(@PathVariable Long id, Model vModel){
        vModel.addAttribute("animal", animalDao.getOne(id));
        return"/index";
    }

    @GetMapping("/animals/{userId}")
    public String showUsersAnimals(@PathVariable Long userId, Model vModel){
        List<Animal> allAnimals = animalDao.findAll();
        List<Animal> usersAnimals = new ArrayList<>();
        for(Animal animal : allAnimals) {
            if(animal.getUser().getId() == userId) {
                usersAnimals.add(animal);
            }
        }
        vModel.addAttribute("usersAnimals", usersAnimals);
        return "/index";
    }
}
