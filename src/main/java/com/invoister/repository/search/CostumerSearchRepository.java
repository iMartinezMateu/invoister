package com.invoister.repository.search;

import com.invoister.domain.Costumer;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Costumer entity.
 */
public interface CostumerSearchRepository extends ElasticsearchRepository<Costumer, Long> {
}
