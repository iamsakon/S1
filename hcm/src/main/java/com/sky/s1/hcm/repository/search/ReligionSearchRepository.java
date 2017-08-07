package com.sky.s1.hcm.repository.search;

import com.sky.s1.hcm.domain.Religion;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Religion entity.
 */
public interface ReligionSearchRepository extends ElasticsearchRepository<Religion, Long> {
}
