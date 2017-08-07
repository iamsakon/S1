package com.sky.s1.hcm.repository.search;

import com.sky.s1.hcm.domain.MaritalStatus;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the MaritalStatus entity.
 */
public interface MaritalStatusSearchRepository extends ElasticsearchRepository<MaritalStatus, Long> {
}
