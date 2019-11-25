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
import org.junit.Assert;
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

    public Animal findAnimalByName(String animalName) {
        return animalDao.findByName(animalName).get(0);
    }

    private User testUser;
    private User testOrganization;
    private UserProfile testUserProfile;
    private Animal animalToView;
    private Animal animalToEdit;
    private Animal animalToDelete;

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
        testUserProfile = userProfileDao.findByFirstName("testUserFirstName");

//        animalToView = findAnimalByName("animalToView");
//        animalToEdit = findAnimalByName("animalToEdit");
//        animalToDelete = findAnimalByName("animalToDelete");

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

        if(testUserProfile == null) {
            UserProfile testUserProfile = new UserProfile();
            testUserProfile.setFirstName("testUserFirstName");
            testUserProfile.setLastName("testUserLastName");
            testUserProfile.setAddress("testUserAddress");
            testUserProfile.setUser(testUser);
            testUserProfile = userProfileDao.save(testUserProfile);
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

        if(animalToView == null) {
            Animal animalToView = new Animal();
            animalToView.setName("animalToView");
            animalToView.setType("dog");
            animalToView.setAge(3);
            animalToView.setSize("large");
            animalToView.setUser(testUser);
            animalDao.save(animalToView);
        }

        if(animalToEdit == null) {
            Animal animalToEdit = new Animal();
            animalToEdit.setName("animalToEdit");
            animalToEdit.setType("dog");
            animalToEdit.setAge(3);
            animalToEdit.setSize("large");
            animalToEdit.setUser(testUser);
            animalDao.save(animalToEdit);
        }

        if(animalToDelete == null) {
            Animal animalToDelete = new Animal();
            animalToDelete.setName("animalToDelete");
            animalToDelete.setType("dog");
            animalToDelete.setAge(3);
            animalToDelete.setSize("large");
            animalToDelete.setUser(testUser);
            animalDao.save(animalToDelete);
        }

    }



    @Test
    public void CreateAnimal() throws Exception {
        this.mvc.perform(
                post("/create-animal")
                        .param("name", "createdAnimal")
                        .param("type", "dog")
                        .param("size", "Large")
                        .param("age", "7"))
                .andExpect(status().is3xxRedirection());


//        createdAnimal.setUser(testUser);


//        animalDao.save(createdAnimal);

        Animal createdAnimal = findAnimalByName("createdAnimal");

        Assert.assertNotNull(createdAnimal);

        animalDao.delete(createdAnimal);
    }


    @Test
    public void showAllAnimals() throws Exception {
        this.mvc.perform(get("/animal/showAll"))
                .andExpect(status().isOk());
    }


    @Test
    public void showAnimal() throws Exception {
        Animal currentAnimal = findAnimalByName("animalToView");
        this.mvc.perform(get("/animal/" + currentAnimal.getId()))
                .andExpect(status().isOk());
    }


    @Test
    public void showUsersAnimals() throws Exception {
        this.mvc.perform(get("/user-profile/" + testUserProfile.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void editAnimal() throws Exception {
        Animal currentAnimal = findAnimalByName("animalToEdit");

        this.mvc.perform(
                post("/animal/" + currentAnimal.getId() + "/edit")
            .param("name", "animalNameEdited")
            .param("size", "medium")
            .param("age", "3"))
                .andExpect(status().is3xxRedirection());

        String editedName = findAnimalByName("animalNameEdited").getName();

        Animal editedAnimal = findAnimalByName("animalNameEdited");

        Assert.assertNotEquals("animalToEdit", editedName);

        animalDao.delete(editedAnimal);

    }

    @Test
    public void deleteAnimal() throws Exception {

        Animal currentAnimal = findAnimalByName("animalToDelete");

        this.mvc.perform(
                post("/delete-animal/" + currentAnimal.getId()))
        .andExpect(status().is3xxRedirection());

        Assert.assertNotEquals("", currentAnimal.getName());
    }

}
