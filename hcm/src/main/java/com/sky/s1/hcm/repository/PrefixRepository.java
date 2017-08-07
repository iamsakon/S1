package com.sky.s1.hcm.repository;

import com.sky.s1.hcm.domain.Prefix;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Prefix entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrefixRepository extends JpaRepository<Prefix,Long> {
    
}
