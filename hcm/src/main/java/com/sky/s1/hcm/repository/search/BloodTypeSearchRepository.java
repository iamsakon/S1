package com.sky.s1.hcm.repository.search;

import com.sky.s1.hcm.domain.BloodType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the BloodType entity.
 */
public interface BloodTypeSearchRepository extends ElasticsearchRepository<BloodType, Long> {
}
