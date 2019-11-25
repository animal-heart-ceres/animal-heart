package com.animalheart.animalheart.controllers;
import com.animalheart.animalheart.models.*;
import com.animalheart.animalheart.repositories.*;
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
    public String createAnimal(@ModelAttribute Animal animal){
//        set fk, will be the current user
        animalDao.save(animal);
        return "redirect:/";
    }

    // JSON
//    @GetMapping("/animal.json")
//    public @ResponseBody List<Animal> showAllAnimalsInJSON() {
//        return animalDao.findAll();
//    }

    @GetMapping("/animal/showAll")
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

        List<Comment> commentList =  animalToDelete.getCommentList();

        for(Comment comment : commentList) {
            comment.setUser(null);
            comment.setAnimal(null);
            commentDao.delete(comment);
        }

        User user = animalDao.getOne(animalId).getUser();

        animalToDelete.setUser(null);

        animalDao.delete(animalToDelete);

        if(user.getOrganization()) {
            OrganizationProfile organizationProfile = organizationProfileDao.findByOrganizationId(user.getId());
            return "redirect:/organization-profile/" + organizationProfile.getId();
        } else {
            UserProfile userProfile = userProfileDao.findByUserId(user.getId());
            return "redirect:/user-profile/" + userProfile.getId();
        }
    }
}
