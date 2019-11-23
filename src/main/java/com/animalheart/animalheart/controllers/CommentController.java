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
import org.springframework.web.bind.annotation.ResponseBody;

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

    @PostMapping("/comment/{commentId}/edit")
    public String editComment(@PathVariable Long commentId, @RequestParam(name = "comment") String editedComment) {
        Comment commentToBeEdited = commentDao.getOne(commentId);

        commentToBeEdited.setComment(editedComment);

        commentDao.save(commentToBeEdited);

        //Gets the animal profile, so that we can redirect to it after edit.
        Animal animalProfileToReturnTo = commentToBeEdited.getAnimal();

        return "redirect:/animal/" + animalProfileToReturnTo.getId();

    }

    @PostMapping("/comment/{commentId}/delete")
    public String deleteComment(@PathVariable Long commentId) {

        Comment commentToBeDeleted = commentDao.getOne(commentId);

        //Gets the animal profile, so that we can redirect to it after delete.
        Animal animalProfileToReturnTo = commentToBeDeleted.getAnimal();

        commentDao.delete(commentToBeDeleted);

        return "redirect:/animal/" + animalProfileToReturnTo.getId();
    }


}
