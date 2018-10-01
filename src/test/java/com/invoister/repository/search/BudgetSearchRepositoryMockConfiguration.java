package com.invoister.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of BudgetSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class BudgetSearchRepositoryMockConfiguration {

    @MockBean
    private BudgetSearchRepository mockBudgetSearchRepository;

}
