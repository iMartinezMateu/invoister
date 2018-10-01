package com.invoister.repository;

import com.invoister.domain.Costumer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Costumer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CostumerRepository extends JpaRepository<Costumer, Long> {

}
