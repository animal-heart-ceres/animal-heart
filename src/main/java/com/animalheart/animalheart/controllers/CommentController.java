package com.animalheart.animalheart.controllers;

import com.animalheart.animalheart.models.Animal;
import com.animalheart.animalheart.models.Comment;
import com.animalheart.animalheart.models.User;
import com.animalheart.animalheart.repositories.AnimalRepository;
import com.animalheart.animalheart.repositories.CommentRepository;
import com.animalheart.animalheart.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CommentController {

    @Autowired
    CommentRepository commentDao;

    @Autowired
    AnimalRepository animalDao;

    @Autowired
    UserRepository userDao;

    @PostMapping("/create-comment/{animalId}/{userId}")
    public String createComment(@PathVariable Long animalId, @PathVariable Long userId, @RequestParam(name = "comment") String comment) {
        Animal animal = animalDao.getOne(animalId);
        User user = userDao.getOne(userId);

        Comment newComment = new Comment();
        newComment.setComment(comment);
        newComment.setAnimal(animal);
        newComment.setUser(user);

        commentDao.save(newComment);

        return "redirect:/";
    }


}
