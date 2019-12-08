package com.animalheart.animalheart;

import com.animalheart.animalheart.models.Animal;
import com.animalheart.animalheart.models.Comment;
import com.animalheart.animalheart.models.User;
import com.animalheart.animalheart.repositories.AnimalRepository;
import com.animalheart.animalheart.repositories.CommentRepository;
import com.animalheart.animalheart.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.containsString;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AnimalHeartApplication.class)
@AutoConfigureMockMvc
public class CommentsIntegrationTests {

    private User testUser;
    private Animal testAnimal;
    private Comment commentToView;
    private Comment commentToEdit;
    private Comment commentToDelete;
    private HttpSession httpSessionUser;
    @Autowired
    UserRepository userDao;

    @Autowired
    AnimalRepository animalDao;

    @Autowired
    private MockMvc mvc;

    @Autowired
    CommentRepository commentDao;

    @Before
    public void setup() throws Exception {

        testUser = userDao.findByUsername("testUser");

        // Creates the test user if not exists
        if (testUser == null) {
            User newUser = new User();
            newUser.setUsername("testUser");
            newUser.setPassword("password");
            newUser.setEmail("testUser@codeup.com");
            newUser.setAdmin(false);
            newUser.setOrganization(false);
            testUser = userDao.save(newUser);
        }

        if (testAnimal == null) {
            Animal newAnimal = new Animal();
            newAnimal.setName("testAnimalName");
            newAnimal.setAge(3);
            newAnimal.setSize("medium");
            newAnimal.setType("dog");
            newAnimal.setUser(testUser);
            testAnimal = animalDao.save(newAnimal);
        }

        if(commentToView == null) {
            Comment commentToView = new Comment();
            commentToView.setComment("This is a comment to view");
            commentToView.setAnimal(testAnimal);
            commentToView.setUserId(testUser.getId());
            commentDao.save(commentToView);
            commentToView = commentDao.findByComment("This is a comment to view").get(0);
        }

        if(commentToDelete == null) {
            Comment commentToDelete = new Comment();
            commentToDelete.setComment("This is a comment to delete");
            commentToDelete.setAnimal(testAnimal);
            commentToDelete.setUserId(testUser.getId());
            commentToDelete = commentDao.save(commentToDelete);
        }

        if(commentToEdit == null) {
            Comment commentToEdit = new Comment();
            commentToEdit.setComment("This is a comment to edit");
            commentToEdit.setAnimal(testAnimal);
            commentToEdit.setUserId(testUser.getId());
            commentToEdit = commentDao.save(commentToEdit);
        }

        httpSessionUser = this.mvc.perform(post("/login").with(csrf())
                .param("username", "testUser")
                .param("password", "password"))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(redirectedUrl("/"))
                .andReturn()
                .getRequest()
                .getSession();

    }

    public Comment findCommentByMessage(String message) {
        return commentDao.findByComment(message).get(0);
    }

    @Test
    public void createComment() throws Exception {
        this.mvc.perform(
                post("/create-comment/" + testAnimal.getId() + "/" + testUser.getId())
                        .with(csrf())
                        .session((MockHttpSession) httpSessionUser)
                    .param("comment", "Test Comment!"))
        .andExpect(status().is3xxRedirection());

        Comment createdComment = findCommentByMessage("Test Comment!");

        Assert.assertNotNull(createdComment);

        commentDao.delete(createdComment);

    }

    @Test
    public void viewComments() throws Exception {
        this.mvc.perform(get("/animal/" + testAnimal.getId())
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(commentToView.getComment())));

    }

    @Test
    public void editComment() throws Exception {
        Comment commentToBeEdited = findCommentByMessage("This is a comment to edit");

        this.mvc.perform(post("/comment/" + commentToBeEdited.getId() + "/edit")
            .param("comment", "This comment has been edited"));

        Comment commentThatWasEdited = findCommentByMessage("This comment has been edited");

        Assert.assertNotEquals("This is a comment to edit", commentThatWasEdited.getComment());

        commentDao.delete(commentThatWasEdited);
    }

    //When the delete button is clicked, it will have that comments ID in the form waiting. So when it  gets to the controller, it will know which comment to delete.
    @Test
    public void deleteComment() throws Exception {

        Comment commentToBeDeleted = findCommentByMessage("This is a comment to delete");

        Assert.assertNotNull(commentToBeDeleted);

        this.mvc.perform(post("/comment/" + commentToBeDeleted.getId() + "/delete"));

        Assert.assertNotEquals("", commentToBeDeleted.getComment());
    }





}
