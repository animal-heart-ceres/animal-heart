package com.animalheart.animalheart;

import com.animalheart.animalheart.models.Follower;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpSession;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AnimalHeartApplication.class)
@AutoConfigureMockMvc
public class FollowersIntegrationTests {

    private User testUser;
    private User testOrganization;
    private Follower followerToDelete;
    private HttpSession httpSessionUser;
    private HttpSession httpSessionOrganization;

    @Autowired
    private MockMvc mvc;

    @Autowired
    FollowerRepository followerDao;

    @Autowired
    UserRepository userDao;

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

        Follower followerToDelete = new Follower();
        followerToDelete.setUser(testOrganization);
        followerToDelete.setFollowerId(testUser.getId());
        followerDao.save(followerToDelete);

    }

    @Test
    public void createFollower() throws Exception {
        this.mvc.perform(
                post("/follower")
                    .param("followerId", Long.toString(testUser.getId())));

        Follower createdFollower = followerDao.findByFollowerId(testUser.getId());

        Assert.assertNotNull(followerDao.findByFollowerId(testUser.getId()));

        followerDao.delete(createdFollower);

    }

    @Test
    public void deleteFollower() throws Exception {
        this.mvc.perform(
                post("/follower/" + testUser.getId() + "/" + testOrganization.getId() + "/delete"));
        Assert.assertNull(followerDao.findByFollowerId(testUser.getId()));
    }

}