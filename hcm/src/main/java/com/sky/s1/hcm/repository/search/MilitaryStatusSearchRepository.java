package com.sky.s1.hcm.repository.search;

import com.sky.s1.hcm.domain.MilitaryStatus;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the MilitaryStatus entity.
 */
public interface MilitaryStatusSearchRepository extends ElasticsearchRepository<MilitaryStatus, Long> {
}
