package com.animalheart.animalheart.models;

import javax.persistence.*;

@Entity
@Table(name = "organization_profiles")
public class OrganizationProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private long taxNumber;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @OneToOne
    private User organization;

    public OrganizationProfile() {}

    public OrganizationProfile(String name, long taxNumber, String description, User organization) {
        this.name = name;
        this.taxNumber = taxNumber;
        this.description = description;
        this.organization = organization;
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

    public long getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(long taxNumber) {
        this.taxNumber = taxNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getOrganization() {
        return organization;
    }

    public void setOrganization(User organization) {
        this.organization = organization;
    }
}
