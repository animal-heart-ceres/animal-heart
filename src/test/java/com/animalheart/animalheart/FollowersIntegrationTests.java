package com.animalheart.animalheart;

import com.animalheart.animalheart.models.Follower;
import com.animalheart.animalheart.models.OrganizationProfile;
import com.animalheart.animalheart.models.User;
import com.animalheart.animalheart.repositories.FollowerRepository;
import com.animalheart.animalheart.repositories.OrganizationProfileRepository;
import com.animalheart.animalheart.repositories.UserProfileRepository;
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

import javax.servlet.http.HttpSession;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AnimalHeartApplication.class)
@AutoConfigureMockMvc
public class FollowersIntegrationTests {

    private User testUser;
    private User testOrganization;
    private Follower followerToDelete;
    private HttpSession httpSessionUser;
    private HttpSession httpSessionOrganization;
    private OrganizationProfile testOrganizationProfile;

    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mvc;

    @Autowired
    FollowerRepository followerDao;

    @Autowired
    UserRepository userDao;

    @Autowired
    OrganizationProfileRepository organizationProfileDao;
    @Before
    public void setup() throws Exception {

        testUser = userDao.findByUsername("testUser");
        testOrganization = userDao.findByUsername("testOrganization");
        followerToDelete = followerDao.findByFollowerId(testUser.getId());
        testOrganizationProfile = organizationProfileDao.findByName("testOrganizationProfileFollow");

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
            newUser.setUsername(passwordEncoder.encode("password"));
            newUser.setPassword("password");
            newUser.setEmail("testOrganization@codeup.com");
            newUser.setAdmin(false);
            newUser.setOrganization(true);
            testOrganization = userDao.save(newUser);
        }

        if(testOrganizationProfile == null) {
            OrganizationProfile testOrganizationProfile = new OrganizationProfile();
            testOrganizationProfile.setName("testOrganizationProfileFollow");
            testOrganizationProfile.setAddress("testOrganizationProfileAddress");
            testOrganizationProfile.setTaxNumber(123456789);
            testOrganizationProfile.setDescription("An organization to test");
            testOrganizationProfile.setOrganization(testOrganization);
            organizationProfileDao.save(testOrganizationProfile);
            testOrganizationProfile = organizationProfileDao.findByName("testOrganizationProfile");
        }

        if (followerToDelete == null) {
            Follower followerToDelete = new Follower();
            followerToDelete.setUser(testOrganization);
            followerToDelete.setFollowerId(testUser.getId());
            followerDao.save(followerToDelete);
            followerToDelete = followerDao.findByFollowerId(testUser.getId());
        }

        httpSessionUser = this.mvc.perform(post("/login").with(csrf())
                .param("username", "testUser")
                .param("password", "password"))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(redirectedUrl("/"))
                .andReturn()
                .getRequest()
                .getSession();

    }

    @Test
    public void createFollower() throws Exception {
        this.mvc.perform(
                post("/follower")
                    .param("followerId", Long.toString(testUser.getId()))
                    .with(csrf()));

        Follower createdFollower = followerDao.findByFollowerId(testUser.getId());

        Assert.assertNotNull(followerDao.findByFollowerId(testUser.getId()));

        followerDao.delete(createdFollower);

    }

//    @Test
//    public void deleteFollower() throws Exception {
//        this.mvc.perform(
//                post("/follower/" + testUser.getId() + "/" + testOrganization.getId() + "/delete")
//                    .with(csrf())
//                    .session((MockHttpSession) httpSessionUser));
//        Assert.assertNull(followerDao.findByFollowerId(testUser.getId()));
//    }

}