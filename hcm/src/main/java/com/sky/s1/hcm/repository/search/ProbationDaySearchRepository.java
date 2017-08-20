package com.sky.s1.hcm.repository.search;

import com.sky.s1.hcm.domain.ProbationDay;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ProbationDay entity.
 */
public interface ProbationDaySearchRepository extends ElasticsearchRepository<ProbationDay, Long> {
}
