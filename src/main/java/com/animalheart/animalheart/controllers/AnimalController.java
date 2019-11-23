package com.animalheart.animalheart.controllers;
import com.animalheart.animalheart.models.Animal;
import com.animalheart.animalheart.models.Comment;
import com.animalheart.animalheart.models.User;
import com.animalheart.animalheart.repositories.AnimalRepository;
import com.animalheart.animalheart.repositories.CommentRepository;
import com.animalheart.animalheart.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class AnimalController {

    @Autowired
    AnimalRepository animalDao;

    @Autowired
    UserRepository userDao;

    @Autowired
    CommentRepository commentDao;

    @GetMapping("/create-animal")
    public String showAnimalForm(Model vModel){
        vModel.addAttribute("animal", new Animal());
        return"/index";
    }

    @PostMapping("/create-animal")
    public String createAnimal(@ModelAttribute Animal animal){
//        set fk, will be the current user
        animalDao.save(animal);
        return "redirect:/";
    }

    // JSON
//    @GetMapping("/animals.json")
//    public @ResponseBody List<Animal> showAllAnimalsInJSON() {
//        return animalDao.findAll();
//    }

    @GetMapping("/animals/showAll")
    public String showAllAnimals(Model vModel) {
        List<Animal> animalList = animalDao.findAll();
        vModel.addAttribute("animalList", animalList);
        return "view-animals";
    }

    @GetMapping("/animal/{id}")
    public String showAnimal(@PathVariable Long id, Model vModel){
        Animal animal = animalDao.getOne(id);
        List<Comment> commentList =  animal.getCommentList();
        vModel.addAttribute("animal", animal);
        vModel.addAttribute("comments", commentList);
        return "animal-profile";
    }

    @PostMapping("/animal/{animalId}/edit")
    public String editAnimal(@PathVariable Long animalId, @RequestParam(name = "name") String name, @RequestParam(name = "size") String size, @RequestParam(name = "age") int age) {
        Animal animalToEdit = animalDao.getOne(animalId);
        animalToEdit.setName(name);
        animalToEdit.setSize(size);
        animalToEdit.setAge(age);
        animalDao.save(animalToEdit);
        return "redirect:/animal/" + animalId;
    }

    @PostMapping("/delete-animal/{animalId}")
    public String deleteAnimal(@PathVariable Long animalId) {

        Animal animalToDelete = animalDao.getOne(animalId);

        User user = animalDao.getOne(animalId).getUser();

        animalDao.delete(animalToDelete);

        if(user.getOrganization()) {
            return "redirect:/organization-profile" + user.getId();
        } else {
            return "redirect:/user-profile/" + user.getId();
        }
    }
}
