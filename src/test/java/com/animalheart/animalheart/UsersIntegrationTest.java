package com.animalheart.animalheart;

import com.animalheart.animalheart.models.User;
import com.animalheart.animalheart.repositories.UserProfileRepository;
import com.animalheart.animalheart.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpSession;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = AnimalHeartApplication.class)
@AutoConfigureMockMvc
public class UsersIntegrationTest {

    private User testUser;
    private User testOrganization;
    private HttpSession httpSession;

    @Autowired
    private MockMvc mvc;

    @Autowired
    UserRepository userDao;


//    @Autowired
//    private PasswordEncoder passwordEncoder;

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


    }

    @Test
    public void testCreateUser() throws Exception {
        // Makes a Post request to /sign-up and expect a redirection to the home
        this.mvc.perform(
                post("/sign-up")
//                        .session((MockHttpSession) httpSession)
                        // Add all the required parameters to your request like this
                        .param("username", "testUserSignUp")
                        .param("email", "testUserSignUp@email.com")
                        .param("password", "testUserSignUpPassword")
//                        .param("isAdmin", "true")
//                        .param("isOrganization", "false")
        )
                .andExpect(status().is3xxRedirection());
    }

//    @Test
//    public void testCreateUserProfile() throws Exception {
//        this.mvc.perform(
//                post("/create-user-profile")
//                .param("firstName", "testFirstName")
//                .param("lastName", "testLastName")
//                .param("address", "testAddress"))
//
//                .andExpect(status().is3xxRedirection());
//    }

}
