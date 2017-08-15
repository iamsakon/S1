package com.sky.s1.hcm.repository.search;

import com.sky.s1.hcm.domain.Candidate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Candidate entity.
 */
public interface CandidateSearchRepository extends ElasticsearchRepository<Candidate, Long> {
}
