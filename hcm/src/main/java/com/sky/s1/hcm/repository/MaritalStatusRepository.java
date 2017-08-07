package com.sky.s1.hcm.repository;

import com.sky.s1.hcm.domain.MaritalStatus;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the MaritalStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaritalStatusRepository extends JpaRepository<MaritalStatus,Long> {
    
}
