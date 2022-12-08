package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.PageLayersDetails;
import com.mycompany.myapp.repository.PageLayersDetailsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PageLayersDetails}.
 */
@Service
@Transactional
public class PageLayersDetailsService {

    private final Logger log = LoggerFactory.getLogger(PageLayersDetailsService.class);

    private final PageLayersDetailsRepository pageLayersDetailsRepository;

    public PageLayersDetailsService(PageLayersDetailsRepository pageLayersDetailsRepository) {
        this.pageLayersDetailsRepository = pageLayersDetailsRepository;
    }

    /**
     * Save a pageLayersDetails.
     *
     * @param pageLayersDetails the entity to save.
     * @return the persisted entity.
     */
    public PageLayersDetails save(PageLayersDetails pageLayersDetails) {
        log.debug("Request to save PageLayersDetails : {}", pageLayersDetails);
        return pageLayersDetailsRepository.save(pageLayersDetails);
    }

    /**
     * Update a pageLayersDetails.
     *
     * @param pageLayersDetails the entity to save.
     * @return the persisted entity.
     */
    public PageLayersDetails update(PageLayersDetails pageLayersDetails) {
        log.debug("Request to update PageLayersDetails : {}", pageLayersDetails);
        return pageLayersDetailsRepository.save(pageLayersDetails);
    }

    /**
     * Partially update a pageLayersDetails.
     *
     * @param pageLayersDetails the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PageLayersDetails> partialUpdate(PageLayersDetails pageLayersDetails) {
        log.debug("Request to partially update PageLayersDetails : {}", pageLayersDetails);

        return pageLayersDetailsRepository
            .findById(pageLayersDetails.getId())
            .map(existingPageLayersDetails -> {
                if (pageLayersDetails.getName() != null) {
                    existingPageLayersDetails.setName(pageLayersDetails.getName());
                }
                if (pageLayersDetails.getDescription() != null) {
                    existingPageLayersDetails.setDescription(pageLayersDetails.getDescription());
                }
                if (pageLayersDetails.getIsActive() != null) {
                    existingPageLayersDetails.setIsActive(pageLayersDetails.getIsActive());
                }

                return existingPageLayersDetails;
            })
            .map(pageLayersDetailsRepository::save);
    }

    /**
     * Get all the pageLayersDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PageLayersDetails> findAll(Pageable pageable) {
        log.debug("Request to get all PageLayersDetails");
        return pageLayersDetailsRepository.findAll(pageable);
    }

    /**
     * Get one pageLayersDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PageLayersDetails> findOne(Long id) {
        log.debug("Request to get PageLayersDetails : {}", id);
        return pageLayersDetailsRepository.findById(id);
    }

    /**
     * Delete the pageLayersDetails by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PageLayersDetails : {}", id);
        pageLayersDetailsRepository.deleteById(id);
    }
}
