package com.invoister.service.impl;

import com.invoister.service.ReportService;
import com.invoister.domain.Report;
import com.invoister.repository.ReportRepository;
import com.invoister.repository.search.ReportSearchRepository;
import com.invoister.service.dto.ReportDTO;
import com.invoister.service.mapper.ReportMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Report.
 */
@Service
@Transactional
public class ReportServiceImpl implements ReportService {

    private final Logger log = LoggerFactory.getLogger(ReportServiceImpl.class);

    private final ReportRepository reportRepository;

    private final ReportMapper reportMapper;

    private final ReportSearchRepository reportSearchRepository;

    public ReportServiceImpl(ReportRepository reportRepository, ReportMapper reportMapper, ReportSearchRepository reportSearchRepository) {
        this.reportRepository = reportRepository;
        this.reportMapper = reportMapper;
        this.reportSearchRepository = reportSearchRepository;
    }

    /**
     * Save a report.
     *
     * @param reportDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ReportDTO save(ReportDTO reportDTO) {
        log.debug("Request to save Report : {}", reportDTO);
        Report report = reportMapper.toEntity(reportDTO);
        report = reportRepository.save(report);
        ReportDTO result = reportMapper.toDto(report);
        reportSearchRepository.save(report);
        return result;
    }

    /**
     * Get all the reports.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ReportDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Reports");
        return reportRepository.findAll(pageable)
            .map(reportMapper::toDto);
    }


    /**
     * Get one report by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ReportDTO> findOne(Long id) {
        log.debug("Request to get Report : {}", id);
        return reportRepository.findById(id)
            .map(reportMapper::toDto);
    }

    /**
     * Delete the report by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Report : {}", id);
        reportRepository.deleteById(id);
        reportSearchRepository.deleteById(id);
    }

    /**
     * Search for the report corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ReportDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Reports for query {}", query);
        return reportSearchRepository.search(queryStringQuery(query), pageable)
            .map(reportMapper::toDto);
    }
}
