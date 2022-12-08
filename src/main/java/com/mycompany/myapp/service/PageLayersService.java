package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.PageLayers;
import com.mycompany.myapp.repository.PageLayersRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PageLayers}.
 */
@Service
@Transactional
public class PageLayersService {

    private final Logger log = LoggerFactory.getLogger(PageLayersService.class);

    private final PageLayersRepository pageLayersRepository;

    public PageLayersService(PageLayersRepository pageLayersRepository) {
        this.pageLayersRepository = pageLayersRepository;
    }

    /**
     * Save a pageLayers.
     *
     * @param pageLayers the entity to save.
     * @return the persisted entity.
     */
    public PageLayers save(PageLayers pageLayers) {
        log.debug("Request to save PageLayers : {}", pageLayers);
        return pageLayersRepository.save(pageLayers);
    }

    /**
     * Update a pageLayers.
     *
     * @param pageLayers the entity to save.
     * @return the persisted entity.
     */
    public PageLayers update(PageLayers pageLayers) {
        log.debug("Request to update PageLayers : {}", pageLayers);
        return pageLayersRepository.save(pageLayers);
    }

    /**
     * Partially update a pageLayers.
     *
     * @param pageLayers the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PageLayers> partialUpdate(PageLayers pageLayers) {
        log.debug("Request to partially update PageLayers : {}", pageLayers);

        return pageLayersRepository
            .findById(pageLayers.getId())
            .map(existingPageLayers -> {
                if (pageLayers.getLayerNo() != null) {
                    existingPageLayers.setLayerNo(pageLayers.getLayerNo());
                }
                if (pageLayers.getIsActive() != null) {
                    existingPageLayers.setIsActive(pageLayers.getIsActive());
                }

                return existingPageLayers;
            })
            .map(pageLayersRepository::save);
    }

    /**
     * Get all the pageLayers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PageLayers> findAll(Pageable pageable) {
        log.debug("Request to get all PageLayers");
        return pageLayersRepository.findAll(pageable);
    }

    /**
     * Get all the pageLayers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PageLayers> findAllWithEagerRelationships(Pageable pageable) {
        return pageLayersRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one pageLayers by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PageLayers> findOne(Long id) {
        log.debug("Request to get PageLayers : {}", id);
        return pageLayersRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the pageLayers by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PageLayers : {}", id);
        pageLayersRepository.deleteById(id);
    }
}
