package com.invoister.service.impl;

import com.invoister.service.NotificatorService;
import com.invoister.domain.Notificator;
import com.invoister.repository.NotificatorRepository;
import com.invoister.repository.search.NotificatorSearchRepository;
import com.invoister.service.dto.NotificatorDTO;
import com.invoister.service.mapper.NotificatorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Notificator.
 */
@Service
@Transactional
public class NotificatorServiceImpl implements NotificatorService {

    private final Logger log = LoggerFactory.getLogger(NotificatorServiceImpl.class);

    private final NotificatorRepository notificatorRepository;

    private final NotificatorMapper notificatorMapper;

    private final NotificatorSearchRepository notificatorSearchRepository;

    public NotificatorServiceImpl(NotificatorRepository notificatorRepository, NotificatorMapper notificatorMapper, NotificatorSearchRepository notificatorSearchRepository) {
        this.notificatorRepository = notificatorRepository;
        this.notificatorMapper = notificatorMapper;
        this.notificatorSearchRepository = notificatorSearchRepository;
    }

    /**
     * Save a notificator.
     *
     * @param notificatorDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public NotificatorDTO save(NotificatorDTO notificatorDTO) {
        log.debug("Request to save Notificator : {}", notificatorDTO);
        Notificator notificator = notificatorMapper.toEntity(notificatorDTO);
        notificator = notificatorRepository.save(notificator);
        NotificatorDTO result = notificatorMapper.toDto(notificator);
        notificatorSearchRepository.save(notificator);
        return result;
    }

    /**
     * Get all the notificators.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<NotificatorDTO> findAll() {
        log.debug("Request to get all Notificators");
        return notificatorRepository.findAll().stream()
            .map(notificatorMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one notificator by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<NotificatorDTO> findOne(Long id) {
        log.debug("Request to get Notificator : {}", id);
        return notificatorRepository.findById(id)
            .map(notificatorMapper::toDto);
    }

    /**
     * Delete the notificator by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Notificator : {}", id);
        notificatorRepository.deleteById(id);
        notificatorSearchRepository.deleteById(id);
    }

    /**
     * Search for the notificator corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<NotificatorDTO> search(String query) {
        log.debug("Request to search Notificators for query {}", query);
        return StreamSupport
            .stream(notificatorSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(notificatorMapper::toDto)
            .collect(Collectors.toList());
    }
}
