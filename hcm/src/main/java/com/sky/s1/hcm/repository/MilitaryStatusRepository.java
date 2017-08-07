package com.sky.s1.hcm.repository;

import com.sky.s1.hcm.domain.MilitaryStatus;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the MilitaryStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MilitaryStatusRepository extends JpaRepository<MilitaryStatus,Long> {
    
}
