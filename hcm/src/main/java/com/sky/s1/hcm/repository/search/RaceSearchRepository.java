package com.sky.s1.hcm.repository.search;

import com.sky.s1.hcm.domain.Race;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Race entity.
 */
public interface RaceSearchRepository extends ElasticsearchRepository<Race, Long> {
}
