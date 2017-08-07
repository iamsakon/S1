package com.sky.s1.hcm.repository.search;

import com.sky.s1.hcm.domain.Gender;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Gender entity.
 */
public interface GenderSearchRepository extends ElasticsearchRepository<Gender, Long> {
}
