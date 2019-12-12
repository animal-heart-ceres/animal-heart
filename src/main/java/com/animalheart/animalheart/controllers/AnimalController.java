package com.animalheart.animalheart.controllers;
import com.animalheart.animalheart.models.*;
import com.animalheart.animalheart.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    OrganizationProfileRepository organizationProfileDao;

    @Autowired
    UserProfileRepository userProfileDao;

    @GetMapping("/create-animal")
    public String showAnimalForm(Model vModel){
        vModel.addAttribute("animal", new Animal());
        return"/";
    }

    @PostMapping("/create-animal")
    public String createAnimal(@ModelAttribute Animal animal) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        animal.setUser(userDao.getOne(user.getId()));
        animalDao.save(animal);

        return "redirect:/user-profile";
    }

    @GetMapping("/animal/showAll")
    public String showAllAnimals(Model vModel) {
        List<Animal> animalList = animalDao.findAll();
        vModel.addAttribute("animalList", animalList);
        return "view-animals";
    }

    @GetMapping("/animal/{id}")
    public String showAnimal(@PathVariable Long id, Model vModel){
        Animal animal = animalDao.getOne(id);

        User organizationThatOwnsThisAnimal = animal.getUser();

        OrganizationProfile organizationProfile = organizationProfileDao.findByOrganizationId(organizationThatOwnsThisAnimal.getId());

        List<Comment> commentList =  animal.getCommentList();

        vModel.addAttribute("organizationProfile", organizationProfile);
        vModel.addAttribute("animal", animal);
        vModel.addAttribute("comment", new Comment());
        vModel.addAttribute("comments", commentList);
        return "animal-profile";
    }

    @PostMapping("/animal/{animalId}/edit")
    public String editAnimal(@PathVariable Long animalId,@RequestParam(name = "type") String type, @RequestParam(name = "name") String name, @RequestParam(name = "size") String size, @RequestParam(name = "age") int age) {
        Animal animalToEdit = animalDao.getOne(animalId);
        animalToEdit.setName(name);
        animalToEdit.setType(type);
        animalToEdit.setSize(size);
        animalToEdit.setAge(age);
        animalDao.save(animalToEdit);
        return "redirect:/animal/" + animalId;
    }

    @PostMapping("/delete-animal/{animalId}")
    public String deleteAnimal(@PathVariable Long animalId) {

        Animal animalToDelete = animalDao.getOne(animalId);
        User user = animalToDelete.getUser();
        List<Animal> animalList = user.getAnimalList();

        for(Animal animal : animalList) {
            if(animal.getId() == animalToDelete.getId()) {
                animalDao.delete(animal);
            }
        }

        return "redirect:/user-profile";
    }
}
