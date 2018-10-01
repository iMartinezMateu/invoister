package com.invoister.repository.search;

import com.invoister.domain.Budget;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Budget entity.
 */
public interface BudgetSearchRepository extends ElasticsearchRepository<Budget, Long> {
}
