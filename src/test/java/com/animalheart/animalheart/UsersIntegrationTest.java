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
import org.springframework.boot.test.context.assertj.AssertableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import javax.servlet.http.HttpSession;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = AnimalHeartApplication.class)
@AutoConfigureMockMvc
public class UsersIntegrationTest {

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
        if(testUser == null){
            User newUser = new User();
            newUser.setUsername("testUser");
            newUser.setPassword("password");
            newUser.setEmail("testUser@codeup.com");
            newUser.setAdmin(true);
            newUser.setOrganization(false);
            testUser = userDao.save(newUser);


        }

        if(testOrganization == null){
            User newUser = new User();
            newUser.setUsername("testOrganization");
            newUser.setPassword("passwordOrg");
            newUser.setEmail("testOrganization@codeup.com");
            newUser.setAdmin(false);
            newUser.setOrganization(true);
            testOrganization = userDao.save(newUser);
        }

//        httpSessionUser = this.mvc.perform(post("/login").with(csrf())
//                .param("username", "testUser")
//                .param("password", "password"))
//                .andExpect(status().is(HttpStatus.FOUND.value()))
//                .andExpect(redirectedUrl("/"))
//                .andReturn()
//                .getRequest()
//                .getSession();

//        httpSessionOrganization = this.mvc.perform(post("/login").with(csrf())
//                .param("username", "testOrganization")
//                .param("password", "passwordOrg"))
//                .andExpect(status().is(HttpStatus.FOUND.value()))
//                .andExpect(redirectedUrl("/"))
//                .andReturn()
//                .getRequest()
//                .getSession();
    }

    @Test
    public void contextLoads() {
        // Sanity Test, just to make sure the MVC bean is working
        assertNotNull(mvc);
    }

//    @Test
//    public void testIfUserSessionIsActive() throws Exception {
//        // It makes sure the returned session is not null
//        assertNotNull(httpSessionUser);
//        assertNotNull(httpSessionOrganization);
//    }

    @Test
    public void testCreateUser() throws Exception {
        // Makes a Post request to /sign-up and expect a redirection to the home
        this.mvc.perform(
                post("/sign-up")
                        .param("username", "testUserSignUp")
                        .param("email", "testUserSignUp@email.com")
                        .param("password", "testUserSignUpPassword")
        )
                .andExpect(status().is3xxRedirection());

        User existingUser = userDao.findByUsername("TestUserSignUp");
        System.out.println("USER WAS CREATED " + existingUser.getUsername());
        userDao.delete(existingUser);
    }

    @Test
    public void testCreateUserProfile() throws Exception {
        this.mvc.perform(
                post("/create-user-profile")
                .param("firstName", "testFirstName")
                .param("lastName", "testLastName")
                .param("address", "testAddress")
                .param("user_id", Long.toString(testUser.getId())))
                .andExpect(status().is3xxRedirection());

        UserProfile currentUserProfile = userProfileDao.findByFirstName("testFirstName");

        userProfileDao.delete(currentUserProfile);
    }



    @Test
    public void testCreateOrganizationProfile() throws Exception {
        this.mvc.perform(
                post("/organization-sign-up")
                        .param("name", "testOrganizationName")
                        .param("taxNumber", "123456789")
                        .param("description", "A San Antonio rescue shelter")
                        .param("address", "600 Navarro St, San Antonio, TX"))
                .andExpect(status().is3xxRedirection());

            OrganizationProfile currentOrganization = organizationProfileDao.findByName("testOrganizationName");
            System.out.println("USER WAS CREATED " + currentOrganization.getName());
            organizationProfileDao.delete(currentOrganization);

    }

    @Test
    public void testViewUserProfile() throws Exception {
        User currentUser = userDao.findByUsername("testUser");

        this.mvc.perform(get("/user-profile/" + currentUser.getId()))
                .andExpect(status().is3xxRedirection());



    }





}
