package com.animalheart.animalheart;


import com.animalheart.animalheart.controllers.AnimalController;
import com.animalheart.animalheart.models.Animal;
import com.animalheart.animalheart.models.User;
import com.animalheart.animalheart.models.UserProfile;
import com.animalheart.animalheart.repositories.AnimalRepository;
import com.animalheart.animalheart.repositories.OrganizationProfileRepository;
import com.animalheart.animalheart.repositories.UserProfileRepository;
import com.animalheart.animalheart.repositories.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AnimalHeartApplication.class)
@AutoConfigureMockMvc
public class AnimalsIntegrationTests {

    private User testUser;
    private User testOrganization;

    @Autowired
    private MockMvc mvc;

    @Autowired
    UserRepository userDao;

    @Autowired
    OrganizationProfileRepository organizationProfileDao;

    @Autowired
    UserProfileRepository userProfileDao;

    @Autowired
    AnimalRepository animalDao;

    @Before
    public void setup() throws Exception {

        testUser = userDao.findByUsername("testUser");
        testOrganization = userDao.findByUsername("testOrganization");

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

        if (testOrganization == null) {
            User newUser = new User();
            newUser.setUsername("testOrganization");
            newUser.setPassword("password");
            newUser.setEmail("testOrganization@codeup.com");
            newUser.setAdmin(false);
            newUser.setOrganization(true);
            testOrganization = userDao.save(newUser);
        }

    }

    @Test
    public void CreateAnimal() throws Exception {
        this.mvc.perform(
                post("/create-animal")
                        .param("name", "testAnimalName")
                        .param("type", "dog")
                        .param("size", "Large")
                        .param("age", "7"))
                .andExpect(status().is3xxRedirection());

        Animal testAnimal = animalDao.findByName("testAnimalName").get(0);

        testAnimal.setUser(testUser);

        animalDao.save(testAnimal);
    }


    @Test
    public void showAllAnimals() throws Exception {

        this.mvc.perform(get("/animals"))
                .andExpect(status().isOk());
    }


    @Test
    public void showAnimal() throws Exception {
        Animal currentAnimal = animalDao.findByName("testAnimalName").get(0);
        System.out.println (currentAnimal.getId());
        this.mvc.perform(get("/animal/" + currentAnimal.getId()))
                .andExpect(status().isOk());
    }


    @Test
    public void showUsersAnimals() throws Exception {

        this.mvc.perform(get("/animals/" + testUser.getId()))
                .andExpect(status().isOk());
    }


    @Test
    public void editAnimal() throws Exception {
        Animal currentAnimal = animalDao.findByName("testAnimalName").get(0);

        this.mvc.perform(
                post("/animal/" + currentAnimal.getId() + "/edit")
            .param("name", "testAnimalNameEdit")
            .param("size", "medium")
            .param("age", "3"))
                .andExpect(status().is3xxRedirection());

    }


    @Test
    public void deleteAnimal() throws Exception {
        Animal currentAnimal = animalDao.findByName("testAnimalNameEdit").get(0);

        this.mvc.perform(
                post("/delete-animal/" + currentAnimal.getId()))
        .andExpect(status().is3xxRedirection());
    }

}
