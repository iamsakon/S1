package com.sky.s1.hcm.repository.search;

import com.sky.s1.hcm.domain.Prefix;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Prefix entity.
 */
public interface PrefixSearchRepository extends ElasticsearchRepository<Prefix, Long> {
}
