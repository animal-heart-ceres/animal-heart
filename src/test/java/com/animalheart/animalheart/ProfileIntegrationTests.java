package com.animalheart.animalheart;

import com.animalheart.animalheart.models.OrganizationProfile;
import com.animalheart.animalheart.models.User;
import com.animalheart.animalheart.models.UserProfile;
import com.animalheart.animalheart.repositories.OrganizationProfileRepository;
import com.animalheart.animalheart.repositories.UserProfileRepository;
import com.animalheart.animalheart.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpSession;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AnimalHeartApplication.class)
@AutoConfigureMockMvc
public class ProfileIntegrationTests {

    private User testUser;
    private User testOrganization;
    private HttpSession httpSessionUser;
    private HttpSession httpSessionOrganization;

    @Autowired
    private MockMvc mvc;

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
    public void CreateUserProfile() throws Exception {
        this.mvc.perform(
                post("/create-user-profile")
                        .param("firstName", "testFirstName")
                        .param("lastName", "testLastName")
                        .param("address", "testAddress"))
                .andExpect(status().is3xxRedirection());

        UserProfile testUserProfile = userProfileDao.findByFirstName("testFirstName");

        testUserProfile.setUser(testUser);

        userProfileDao.save(testUserProfile);
    }

    @Test
    public void CreateOrganizationProfile() throws Exception {
        this.mvc.perform(
                post("/create-organization-profile")
                        .param("name", "testOrganizationName")
                        .param("taxNumber", "123456789")
                        .param("description", "A San Antonio rescue shelter")
                        .param("address", "600 Navarro St, San Antonio, TX"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void ViewUserProfile() throws Exception {
        User currentUser = userDao.findByUsername("testUser");
        this.mvc.perform(get("/user-profile/" + currentUser.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void ViewOrganizationProfile() throws Exception {
        User currentUser = userDao.findByUsername("testOrganization");
        this.mvc.perform(get("/user-profile/" + currentUser.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void EditUserProfile() throws Exception{
        UserProfile currentUserProfile = userProfileDao.findByFirstName("testFirstName");

        this.mvc.perform(
                post("/profile/" + currentUserProfile.getId() + "/edit")
                        .param("firstName", "FirstNameEdit")
                        .param("lastName", "LastNameEdit")
                        .param("address", "AddressEdit"))
                .andExpect(status().is3xxRedirection());

        UserProfile editedTestUserProfile = userProfileDao.findByFirstName("FirstNameEdit");

        userProfileDao.delete(editedTestUserProfile);

//        this.mvc.perform(get("/profile/" + testUser.getId()))
//                .andExpect(status().isOk())
//                // Test the dynamic content of the page
//                .andExpect(content().string(containsString("FirstNameEdit")))
//                .andExpect(content().string(containsString("LastNameEdit")))
//                .andExpect(content().string(containsString("AddressEdit")));

    }

    @Test
    public void EditOrganizationProfile() throws Exception{

        OrganizationProfile currentTestOrganizationProfile = organizationProfileDao.findByName("testOrganizationName");

        this.mvc.perform(
                post("/organization-profile/" + currentTestOrganizationProfile.getId() + "/edit")
                        .param("name", "testOrganizationNameEdit")
                        .param("taxNumber", "12333333")
                        .param("address", "testOrganizationAddressEdit")
                        .param("description", "A very good test organization"))
                .andExpect(status().is3xxRedirection());

        OrganizationProfile editedTestOrganizationProfile = organizationProfileDao.findByName("testOrganizationNameEdit");

        organizationProfileDao.delete(editedTestOrganizationProfile);
    }
}
