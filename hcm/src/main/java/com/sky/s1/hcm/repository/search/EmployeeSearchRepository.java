package com.sky.s1.hcm.repository.search;

import com.sky.s1.hcm.domain.Employee;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Employee entity.
 */
public interface EmployeeSearchRepository extends ElasticsearchRepository<Employee, Long> {
}
