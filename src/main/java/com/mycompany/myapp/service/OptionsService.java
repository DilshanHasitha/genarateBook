package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Options;
import com.mycompany.myapp.repository.OptionsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Options}.
 */
@Service
@Transactional
public class OptionsService {

    private final Logger log = LoggerFactory.getLogger(OptionsService.class);

    private final OptionsRepository optionsRepository;

    public OptionsService(OptionsRepository optionsRepository) {
        this.optionsRepository = optionsRepository;
    }

    /**
     * Save a options.
     *
     * @param options the entity to save.
     * @return the persisted entity.
     */
    public Options save(Options options) {
        log.debug("Request to save Options : {}", options);
        return optionsRepository.save(options);
    }

    /**
     * Update a options.
     *
     * @param options the entity to save.
     * @return the persisted entity.
     */
    public Options update(Options options) {
        log.debug("Request to update Options : {}", options);
        return optionsRepository.save(options);
    }

    /**
     * Partially update a options.
     *
     * @param options the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Options> partialUpdate(Options options) {
        log.debug("Request to partially update Options : {}", options);

        return optionsRepository
            .findById(options.getId())
            .map(existingOptions -> {
                if (options.getCode() != null) {
                    existingOptions.setCode(options.getCode());
                }
                if (options.getDescription() != null) {
                    existingOptions.setDescription(options.getDescription());
                }
                if (options.getImgURL() != null) {
                    existingOptions.setImgURL(options.getImgURL());
                }
                if (options.getIsActive() != null) {
                    existingOptions.setIsActive(options.getIsActive());
                }

                return existingOptions;
            })
            .map(optionsRepository::save);
    }

    /**
     * Get all the options.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Options> findAll(Pageable pageable) {
        log.debug("Request to get all Options");
        return optionsRepository.findAll(pageable);
    }

    /**
     * Get all the options with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Options> findAllWithEagerRelationships(Pageable pageable) {
        return optionsRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one options by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Options> findOne(Long id) {
        log.debug("Request to get Options : {}", id);
        return optionsRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the options by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Options : {}", id);
        optionsRepository.deleteById(id);
    }
}
