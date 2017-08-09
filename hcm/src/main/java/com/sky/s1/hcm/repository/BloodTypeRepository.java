package com.sky.s1.hcm.repository;

import com.sky.s1.hcm.domain.BloodType;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the BloodType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BloodTypeRepository extends JpaRepository<BloodType,Long> {
    
}
