package com.sky.s1.hcm.repository;

import com.sky.s1.hcm.domain.EmployeeContact;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the EmployeeContact entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeeContactRepository extends JpaRepository<EmployeeContact,Long> {
    
}
