package com.sky.s1.hcm.repository.search;

import com.sky.s1.hcm.domain.ProbationPeriod;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ProbationPeriod entity.
 */
public interface ProbationPeriodSearchRepository extends ElasticsearchRepository<ProbationPeriod, Long> {
}
