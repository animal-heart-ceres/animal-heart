package com.animalheart.animalheart;

import com.animalheart.animalheart.models.Animal;
import com.animalheart.animalheart.models.Event;
import com.animalheart.animalheart.models.User;
import com.animalheart.animalheart.repositories.EventRepository;
import com.animalheart.animalheart.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.transaction.TransactionScoped;
import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AnimalHeartApplication.class)
@AutoConfigureMockMvc
public class EventIntegrationTests {


    private User testUser;
    private User testOrganization;

    @Autowired
    private MockMvc mvc;

    @Autowired
    UserRepository userDao;

    @Autowired
    EventRepository eventDao;



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
    public void CreateEvent() throws Exception {
        this.mvc.perform(
                post("/create-event")
                        .param("title", "testEvent")
                        .param("description", "test event description")
                        .param("location", "600 Navarrow, San Antonio, TX"))
                .andExpect(status().is3xxRedirection());

        Event testEvent = eventDao.findByTitle("testEvent");

        testEvent.setUser(testUser);

        eventDao.save(testEvent);
    }

    @Test
    public void showAllEvents() throws Exception {

        this.mvc.perform(get("/events"))
                .andExpect(status().isOk());
    }

    @Test
    public void showEvent() throws Exception {
        Event currentEvent = eventDao.findByTitle("testEvent");

        this.mvc.perform(get("/event/" + currentEvent.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void showUsersEvents() throws Exception {

        this.mvc.perform(get("/events/" + testUser.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void editEvent() throws Exception {
        Event currentEvent = eventDao.findByTitle("testEvent");
        this.mvc.perform(
                post("/event/" + currentEvent.getId() + "/edit")
                        .param("title", "testEventEdited")
                        .param("description", "test event description Edited")
                        .param("location", "600 Navarrow, San Antonio, TX Edited"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void deleteEvent() throws Exception {
        Event currentEvent = eventDao.findByTitle("testEventEdited");

        this.mvc.perform(
                post("/event/" + currentEvent.getId() + "/delete"))
                .andExpect(status().is3xxRedirection());
    }

}
