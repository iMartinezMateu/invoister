package com.invoister.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of CompanySearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class CompanySearchRepositoryMockConfiguration {

    @MockBean
    private CompanySearchRepository mockCompanySearchRepository;

}
