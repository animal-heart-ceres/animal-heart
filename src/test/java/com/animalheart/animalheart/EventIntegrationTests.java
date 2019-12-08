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
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.servlet.http.HttpSession;
import javax.transaction.TransactionScoped;
import javax.transaction.Transactional;
import static org.hamcrest.CoreMatchers.containsString;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    private HttpSession httpSessionUser;
    private HttpSession httpSessionOrganization;

    @Autowired
    private MockMvc mvc;

    private PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userDao;

    @Autowired
    EventRepository eventDao;

    @Autowired
    OrganizationProfileRepository organizationProfileDao;



    @Before
    public void setup() throws Exception {

        testUser = userDao.findByUsername("testUser");
        testOrganization = userDao.findByUsername("testOrganization");
        testOrganizationProfile = organizationProfileDao.findByName("testOrganizationProfile");
        eventToView = eventDao.findByTitle("eventToView");
        eventToEdit = eventDao.findByTitle("eventToEdit");
        eventToDelete = eventDao.findByTitle("eventToDelete");

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
            testOrganizationProfile.setAddress("testOrganizationProfileAddress");
            testOrganizationProfile.setTaxNumber(123456789);
            testOrganizationProfile.setDescription("An organization to test");
            testOrganizationProfile.setOrganization(testOrganization);
            organizationProfileDao.save(testOrganizationProfile);
            testOrganizationProfile = organizationProfileDao.findByName("testOrganizationProfile");
        }

        if (eventToView == null) {
            Event eventToView = new Event();
            eventToView.setTitle("eventToView");
            eventToView.setDescription("An event to view");
            eventToView.setLocation("eventToViewLocation");
            eventToView.setUser(testOrganization);
            eventDao.save(eventToView);
            eventToView = eventDao.findByTitle("eventToView");
        }

        if (eventToEdit == null) {
            Event eventToEdit = new Event();
            eventToEdit.setTitle("eventToEdit");
            eventToEdit.setDescription("An event to Edit");
            eventToEdit.setLocation("eventToEditLocation");
            eventToEdit.setUser(testOrganization);
            eventDao.save(eventToEdit);
            eventToEdit = eventDao.findByTitle("eventToEdit");
        }

        if (eventToDelete == null) {
            Event eventToDelete = new Event();
            eventToDelete.setTitle("eventToDelete");
            eventToDelete.setDescription("An event to Delete");
            eventToDelete.setLocation("eventToDeleteLocation");
            eventToDelete.setUser(testOrganization);
            eventDao.save(eventToDelete);
            eventToDelete = eventDao.findByTitle("eventToDelete");
        }

        httpSessionUser = this.mvc.perform(post("/login").with(csrf())
                .param("username", "testUser")
                .param("password", "password"))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(redirectedUrl("/"))
                .andReturn()
                .getRequest()
                .getSession();

        httpSessionOrganization = this.mvc.perform(post("/login").with(csrf())
                .param("username", "testOrganization")
                .param("password", "password"))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(redirectedUrl("/"))
                .andReturn()
                .getRequest()
                .getSession();
    }

    @Test
    public void CreateEvent() throws Exception {
        this.mvc.perform(
                post("/create-event")
                        .with(csrf())
                        .session((MockHttpSession) httpSessionOrganization)
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

        this.mvc.perform(get("/events/showAll")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("eventToView")));
    }

    @Test
    public void showEvent() throws Exception {
        Event eventToView = eventDao.findByTitle("eventToView");
        this.mvc.perform(get("/event-profile/" + eventToView.getId())
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("eventToView")));
    }

    @Test
    public void showUsersEvents() throws Exception {
        OrganizationProfile testOrganizationProfile = organizationProfileDao.findByName("testOrganizationProfile");
        this.mvc.perform(get("/organization-profile/" + testOrganizationProfile.getId())
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("eventToView")));
    }

    @Test
    public void editEvent() throws Exception {
        Event currentEvent = eventDao.findByTitle("eventToEdit");
        this.mvc.perform(
                post("/event/" + currentEvent.getId() + "/edit")
                        .with(csrf())
                        .session((MockHttpSession) httpSessionOrganization)
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
        Event currentEvent = eventDao.findByTitle("eventToDelete");

        this.mvc.perform(
                post("/event/" + currentEvent.getId() + "/delete")
                        .with(csrf())
                        .session((MockHttpSession) httpSessionOrganization))
                .andExpect(status().is3xxRedirection());

        Assert.assertNotNull(currentEvent);
    }

}
