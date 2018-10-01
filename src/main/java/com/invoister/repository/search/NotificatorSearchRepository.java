package com.invoister.repository.search;

import com.invoister.domain.Notificator;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Notificator entity.
 */
public interface NotificatorSearchRepository extends ElasticsearchRepository<Notificator, Long> {
}
