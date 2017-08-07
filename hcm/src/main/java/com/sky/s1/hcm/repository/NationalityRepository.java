package com.sky.s1.hcm.repository;

import com.sky.s1.hcm.domain.Nationality;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Nationality entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NationalityRepository extends JpaRepository<Nationality,Long> {
    
}
