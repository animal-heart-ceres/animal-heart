package com.animalheart.animalheart;

import com.animalheart.animalheart.models.OrganizationProfile;
import com.animalheart.animalheart.models.User;
import com.animalheart.animalheart.models.UserProfile;
import com.animalheart.animalheart.repositories.OrganizationProfileRepository;
import com.animalheart.animalheart.repositories.UserProfileRepository;
import com.animalheart.animalheart.repositories.UserRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AnimalHeartApplication.class)
@AutoConfigureMockMvc
public class ProfileIntegrationTests {

    private User testUser;
    private User testOrganization;
    private UserProfile userProfileToView;
    private UserProfile userProfileToEdit;
    private UserProfile userProfileToDelete;
    private OrganizationProfile organizationProfileToView;
    private OrganizationProfile organizationProfileToEdit;
    private OrganizationProfile organizationProfileToDelete;

    private HttpSession httpSessionUser;
    private HttpSession httpSessionOrganization;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userDao;

    @Autowired
    OrganizationProfileRepository organizationProfileDao;

    @Autowired
    UserProfileRepository userProfileDao;

    @Before
    public void setup() throws Exception {

        testUser = userDao.findByUsername("testUser");
        testOrganization = userDao.findByUsername("testOrganization");

        userProfileToView = userProfileDao.findByFirstName("userProfileToViewFirstName");
        userProfileToEdit = userProfileDao.findByFirstName("userProfileToEditFirstName");
        userProfileToDelete = userProfileDao.findByFirstName("userProfileToDeleteFirstName");

        organizationProfileToView = organizationProfileDao.findByName("organizationProfileToViewName");
        organizationProfileToEdit = organizationProfileDao.findByName("organizationProfileToEditName");
        organizationProfileToDelete = organizationProfileDao.findByName("organizationProfileToDeleteName");

        // Creates the test user if not exists
        if (testUser == null) {
            User newUser = new User();
            newUser.setUsername("testUser");
            newUser.setPassword(passwordEncoder.encode("password"));
            newUser.setEmail("testUser@codeup.com");
            newUser.setAdmin(false);
            newUser.setOrganization(false);
            testUser = userDao.save(newUser);
        }

        if (testOrganization == null) {
            User newUser = new User();
            newUser.setUsername("testOrganization");
            newUser.setPassword(passwordEncoder.encode("password"));
            newUser.setEmail("testOrganization@codeup.com");
            newUser.setAdmin(false);
            newUser.setOrganization(true);
            testOrganization = userDao.save(newUser);
        }

        if(userProfileToView == null) {
            UserProfile userProfileToView = new UserProfile();
            userProfileToView.setFirstName("userProfileToViewFirstName");
            userProfileToView.setLastName("userProfileToViewLastName");
            userProfileToView.setAddress("userProfileToViewAddress");
            userProfileToView.setUser(testUser);
            userProfileToView = userProfileDao.save(userProfileToView);
        }

        if(userProfileToEdit == null) {
            UserProfile userProfileToEdit = new UserProfile();
            userProfileToEdit.setFirstName("userProfileToEditFirstName");
            userProfileToEdit.setLastName("userProfileToEditLastName");
            userProfileToEdit.setAddress("userProfileToEditAddress");
            userProfileToEdit.setUser(testUser);
            userProfileToEdit = userProfileDao.save(userProfileToEdit);
        }

        if(userProfileToDelete == null) {
            UserProfile userProfileToDelete = new UserProfile();
            userProfileToDelete.setFirstName("userProfileToDeleteFirstName");
            userProfileToDelete.setLastName("userProfileToDeleteLastName");
            userProfileToDelete.setAddress("userProfileToDeleteAddress");
            userProfileToDelete.setUser(testUser);
            userProfileToDelete = userProfileDao.save(userProfileToDelete);
        }

        if(organizationProfileToView == null) {
            OrganizationProfile organizationProfileToView = new OrganizationProfile();
            organizationProfileToView.setName("organizationProfileToViewName");
            organizationProfileToView.setAddress("organizationProfileToViewAddress");
            organizationProfileToView.setTaxNumber(123456789);
            organizationProfileToView.setDescription("An organization to view");
            organizationProfileToView.setOrganization(testOrganization);
            organizationProfileToView = organizationProfileDao.save(organizationProfileToView);
        }

        if(organizationProfileToEdit == null) {
            OrganizationProfile organizationProfileToEdit = new OrganizationProfile();
            organizationProfileToEdit.setName("organizationProfileToEditName");
            organizationProfileToEdit.setAddress("organizationProfileToEditAddress");
            organizationProfileToEdit.setTaxNumber(123456789);
            organizationProfileToEdit.setDescription("An organization to edit");
            organizationProfileToEdit.setOrganization(testOrganization);
            organizationProfileToEdit = organizationProfileDao.save(organizationProfileToEdit);
        }

        if(organizationProfileToDelete == null) {
            OrganizationProfile organizationProfileToDelete = new OrganizationProfile();
            organizationProfileToDelete.setName("organizationProfileToDeleteName");
            organizationProfileToDelete.setAddress("organizationProfileToDeleteAddress");
            organizationProfileToDelete.setTaxNumber(123456789);
            organizationProfileToDelete.setDescription("An organization to delete");
            organizationProfileToDelete.setOrganization(testOrganization);
            organizationProfileToDelete = organizationProfileDao.save(organizationProfileToDelete);
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
    @Order(1)
    public void contextLoads() {
        // Sanity Test, just to make sure the MVC bean is working
        assertNotNull(mvc);
    }

    public UserProfile findUserProfileByFirstName(String firstName) {
        return userProfileDao.findByFirstName(firstName);
    }

    public OrganizationProfile findOrganizationProfileByName(String name) {
        return organizationProfileDao.findByName(name);
    }

    @Test
    public void CreateUserProfile() throws Exception {
        String testUserId = Long.toString(testUser.getId());
        this.mvc.perform(
                post("/create-user-profile").with(csrf())
                        .param("firstName", "testFirstName")
                        .param("lastName", "testLastName")
                        .param("address", "testAddress")
                        .param("userId", testUserId))
                .andExpect(status().is3xxRedirection());

        UserProfile createdUserProfile = findUserProfileByFirstName("testFirstName");

        createdUserProfile.setUser(testUser);

        userProfileDao.save(createdUserProfile);

        Assert.assertNotNull(createdUserProfile);

        userProfileDao.delete(createdUserProfile);

    }

    @Test
    public void CreateOrganizationProfile() throws Exception {
        String organizationId = Long.toString(testOrganization.getId());
        this.mvc.perform(
                post("/create-organization-profile").with(csrf())
                        .param("name", "testOrganizationName")
                        .param("taxNumber", "123456789")
                        .param("description", "A San Antonio rescue shelter")
                        .param("address", "600 Navarro St, San Antonio, TX")
                        .param("organizationId", organizationId))
                .andExpect(status().is3xxRedirection());

        OrganizationProfile createdOrganizationProfile = findOrganizationProfileByName("testOrganizationName");

        Assert.assertNotNull(createdOrganizationProfile);

        organizationProfileDao.delete(createdOrganizationProfile);
    }

//    @Test
//    public void ViewUserProfile() throws Exception {
//        this.mvc.perform(get("/user-profile/" + userProfileToView.getId())
//                .with(csrf())
//                .session((MockHttpSession) httpSessionUser))
//                .andExpect(status().isOk())
//                .andExpect(content().string(containsString(userProfileToView.getFirstName())));
//    }

    @Test
    public void ViewOrganizationProfile() throws Exception {
        this.mvc.perform(get("/organization-profile/" + organizationProfileToView.getId())
                .with(csrf())
                .session((MockHttpSession) httpSessionOrganization))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(organizationProfileToView.getName())));
    }

    @Test
    public void EditUserProfile() throws Exception{

        this.mvc.perform(
                post("/profile/" + userProfileToEdit.getId() + "/edit")
                        .with(csrf())
                        .session((MockHttpSession) httpSessionOrganization)
                        .param("firstName", "FirstNameEdited")
                        .param("lastName", "LastNameEdited")
                        .param("address", "AddressEdited"))
                .andExpect(status().is3xxRedirection());

        UserProfile editedTestUserProfile = userProfileDao.findByFirstName("FirstNameEdited");

        Assert.assertNotEquals("userProfileToEditFirstName", "FirstNameEdited");

        userProfileDao.delete(editedTestUserProfile);

    }

    @Test
    public void EditOrganizationProfile() throws Exception{

        this.mvc.perform(
                post("/organization-profile/" + organizationProfileToEdit.getId() + "/edit")
                        .with(csrf())
                        .session((MockHttpSession) httpSessionOrganization)
                        .param("name", "organizationNameEdited")
                        .param("taxNumber", "12333333")
                        .param("address", "organizationAddressEdited")
                        .param("description", "Organization profile was edited"))
                .andExpect(status().is3xxRedirection());

        OrganizationProfile editedTestOrganizationProfile = organizationProfileDao.findByName("organizationNameEdited");

        organizationProfileDao.delete(editedTestOrganizationProfile);
    }

//    @AfterAll
//    public void ensureDatabaseIsCleared() {
//        UserProfile userProfileToView = userProfileDao.findByFirstName("userProfileToViewFirstName");
//        userProfileDao.delete(userProfileToView);
//
//        UserProfile userProfileToEdit = userProfileDao.findByFirstName("userProfileToEditFirstName");
//        userProfileDao.delete(userProfileToEdit);
//
//        UserProfile userProfileToDelete = userProfileDao.findByFirstName("userProfileToDeleteFirstName");
//        userProfileDao.delete(userProfileToDelete);
//
//        OrganizationProfile organizationProfileToView = organizationProfileDao.findByName("organizationProfileToViewName");
//        organizationProfileDao.delete(organizationProfileToView);
//
//        OrganizationProfile organizationProfileToEdit = organizationProfileDao.findByName("organizationProfileToEditName");
//        organizationProfileDao.delete(organizationProfileToEdit);
//
//        OrganizationProfile organizationProfileToDelete = organizationProfileDao.findByName("organizationProfileToDeleteName");
//        organizationProfileDao.delete(organizationProfileToDelete);
//    }


}
