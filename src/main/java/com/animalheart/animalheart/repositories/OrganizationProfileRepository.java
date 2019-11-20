package com.animalheart.animalheart.repositories;

import com.animalheart.animalheart.models.OrganizationProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationProfileRepository extends JpaRepository<OrganizationProfile, Long> {
    OrganizationProfile findByName(String name);
}
