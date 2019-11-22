package com.animalheart.animalheart;

import com.animalheart.animalheart.models.Animal;
import com.animalheart.animalheart.models.Comment;
import com.animalheart.animalheart.models.User;
import com.animalheart.animalheart.repositories.AnimalRepository;
import com.animalheart.animalheart.repositories.CommentRepository;
import com.animalheart.animalheart.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AnimalHeartApplication.class)
@AutoConfigureMockMvc
@Transactional
public class CommentsIntegrationTests {

    private User testUser;
    private Animal testAnimal;

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
        testAnimal = animalDao.findByName("testAnimalName");

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



    }

    @Test
    public void createComment() throws Exception {
        this.mvc.perform(
                post("/create-comment/" + testAnimal.getId() + "/" + testUser.getId())
                    .param("comment", "Test Comment!"))
        .andExpect(status().is3xxRedirection());

    }

    @Test
    public void viewComments() throws Exception {
        Animal testAnimal = animalDao.findByName("testAnimalName");
        this.mvc.perform(get("/animal/" + testAnimal.getId()))
                .andExpect(status().isOk());
    }
}
