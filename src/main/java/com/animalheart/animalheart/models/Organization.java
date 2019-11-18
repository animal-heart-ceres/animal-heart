package com.animalheart.animalheart.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "organizations")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String org_name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 500)
    private String password;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "organization")
    private List<Animal> animalList;

    @ManyToMany(mappedBy = "organizations")
    private List<User> users;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "organization")
    private List<Comment> commentList;

    public Organization() {}

    public Organization(String org_name, String email, String password) {
        this.org_name = org_name;
        this.email = email;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
