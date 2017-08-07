package com.sky.s1.hcm.repository;

import com.sky.s1.hcm.domain.Religion;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Religion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReligionRepository extends JpaRepository<Religion,Long> {
    
}
