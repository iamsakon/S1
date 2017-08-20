package com.sky.s1.hcm.repository;

import com.sky.s1.hcm.domain.ProbationPeriod;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ProbationPeriod entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProbationPeriodRepository extends JpaRepository<ProbationPeriod,Long> {
    
}
