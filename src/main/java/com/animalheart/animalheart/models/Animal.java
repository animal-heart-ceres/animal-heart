package com.animalheart.animalheart.models;

import net.bytebuddy.implementation.bind.annotation.Default;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
public class Animal {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    @Value("unknown")
    private String name;

    @Column
    private String img;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String size;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private String exoticType;

    @Column(columnDefinition = "DATETIME")
    private Date dateCreated;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "animal")
    private List<Animal> animals;

    @ManyToOne
    @JoinColumn (name = "user_id")
    private User user;

    public Animal(){}

    public Animal(String img, String type, String size, int age, String exoticType) {
        this.img = img;
        this.type = type;
        this.size = size;
        this.age = age;
        this.exoticType = exoticType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getExoticType() {
        return exoticType;
    }

    public void setExoticType(String exoticType) {
        this.exoticType = exoticType;
    }
}
