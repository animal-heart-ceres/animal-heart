package com.animalheart.animalheart;

import com.animalheart.animalheart.models.Animal;
import com.animalheart.animalheart.models.Event;
import com.animalheart.animalheart.models.OrganizationProfile;
import com.animalheart.animalheart.models.User;
import com.animalheart.animalheart.repositories.EventRepository;
import com.animalheart.animalheart.repositories.OrganizationProfileRepository;
import com.animalheart.animalheart.repositories.UserRepository;
import org.junit.Assert;
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
    private OrganizationProfile testOrganizationProfile;
    private Event eventToView;
    private Event eventToEdit;
    private Event eventToDelete;

    @Autowired
    private MockMvc mvc;

    @Autowired
    UserRepository userDao;

    @Autowired
    EventRepository eventDao;

    @Autowired
    OrganizationProfileRepository organizationProfileDao;



    @Before
    public void setup() throws Exception {

//        testUser = userDao.findByUsername("testUser");
//        testOrganization = userDao.findByUsername("testOrganization");
//        eventToView = eventDao.findByTitle("eventToView");

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

        if(testOrganizationProfile == null) {
            OrganizationProfile testOrganizationProfile = new OrganizationProfile();
            testOrganizationProfile.setName("testOrganizationProfile");
            testOrganizationProfile.setAddress("testOrganizationProfileTo");
            testOrganizationProfile.setTaxNumber(123456789);
            testOrganizationProfile.setDescription("An organization to test");
            testOrganizationProfile.setOrganization(testOrganization);
            testOrganizationProfile = organizationProfileDao.save(testOrganizationProfile);
        }

        if (eventToView == null) {
            Event eventToView = new Event();
            eventToView.setTitle("eventToView");
            eventToView.setDescription("An event to view");
            eventToView.setLocation("eventToViewLocation");
            eventToView.setUser(testOrganization);
            eventToView = eventDao.save(eventToView);
        }

        if (eventToEdit == null) {
            Event eventToEdit = new Event();
            eventToEdit.setTitle("eventToEdit");
            eventToEdit.setDescription("An event to Edit");
            eventToEdit.setLocation("eventToEditLocation");
            eventToEdit.setUser(testOrganization);
            eventToEdit = eventDao.save(eventToEdit);
        }

        if (eventToDelete == null) {
            Event eventToDelete = new Event();
            eventToDelete.setTitle("eventToDelete");
            eventToDelete.setDescription("An event to Delete");
            eventToDelete.setLocation("eventToDeleteLocation");
            eventToDelete.setUser(testOrganization);
            eventToDelete = eventDao.save(eventToDelete);
        }
    }

    @Test
    public void CreateEvent() throws Exception {
        this.mvc.perform(
                post("/create-event")
                        .param("title", "testEvent")
                        .param("description", "test event description")
                        .param("location", "600 Navarro, San Antonio, TX"))
                .andExpect(status().is3xxRedirection());

        Event createdEvent = eventDao.findByTitle("testEvent");

        createdEvent.setUser(testOrganization);

        Assert.assertNotNull(createdEvent);

        eventDao.delete(createdEvent);
    }

    @Test
    public void showAllEvents() throws Exception {

        this.mvc.perform(get("/events"))
                .andExpect(status().isOk());
    }

    @Test
    public void showEvent() throws Exception {
        Event eventToView = eventDao.findByTitle("eventToView");
        this.mvc.perform(get("/event-profile/" + eventToView.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void showUsersEvents() throws Exception {
        OrganizationProfile testOrganizationProfile = organizationProfileDao.findByName("eventToView");
        this.mvc.perform(get("/organization-profile/" + testOrganizationProfile.getId()))
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

        Event eventThatWasEdited = eventDao.findByTitle("testEventEdited");

        Assert.assertNotEquals("An event to Edit", eventThatWasEdited.getDescription());

        eventDao.delete(eventThatWasEdited);
    }

    @Test
    public void deleteEvent() throws Exception {
        Event currentEvent = eventDao.findByTitle("testEventEdited");

        this.mvc.perform(
                post("/event/" + currentEvent.getId() + "/delete"))
                .andExpect(status().is3xxRedirection());
    }

}
