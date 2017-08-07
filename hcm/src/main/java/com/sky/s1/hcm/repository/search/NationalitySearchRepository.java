package com.sky.s1.hcm.repository.search;

import com.sky.s1.hcm.domain.Nationality;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Nationality entity.
 */
public interface NationalitySearchRepository extends ElasticsearchRepository<Nationality, Long> {
}
