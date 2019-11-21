package com.animalheart.animalheart.models;

import javax.persistence.*;

@Entity
@Table(name = "followers")
public class Follower {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private long followerId;

    @ManyToOne
    @JoinColumn (name = "user_organization_id")
    private User user;

    public Follower() {}

    public Follower(long followerId) {
        this.followerId = followerId;
    }

    public Follower(long followerId, User user) {
        this.followerId = followerId;
        this.user = user;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getFollowerId() {
        return followerId;
    }

    public void setFollowerId(long followerId) {
        this.followerId = followerId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
