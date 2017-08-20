package com.sky.s1.hcm.repository.search;

import com.sky.s1.hcm.domain.EmployeeContact;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the EmployeeContact entity.
 */
public interface EmployeeContactSearchRepository extends ElasticsearchRepository<EmployeeContact, Long> {
}
