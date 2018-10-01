package com.invoister.repository;

import com.invoister.domain.Notificator;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Notificator entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificatorRepository extends JpaRepository<Notificator, Long> {

}
