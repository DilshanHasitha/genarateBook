package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.PriceRelatedOption;
import com.mycompany.myapp.repository.PriceRelatedOptionRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PriceRelatedOption}.
 */
@Service
@Transactional
public class PriceRelatedOptionService {

    private final Logger log = LoggerFactory.getLogger(PriceRelatedOptionService.class);

    private final PriceRelatedOptionRepository priceRelatedOptionRepository;

    public PriceRelatedOptionService(PriceRelatedOptionRepository priceRelatedOptionRepository) {
        this.priceRelatedOptionRepository = priceRelatedOptionRepository;
    }

    /**
     * Save a priceRelatedOption.
     *
     * @param priceRelatedOption the entity to save.
     * @return the persisted entity.
     */
    public PriceRelatedOption save(PriceRelatedOption priceRelatedOption) {
        log.debug("Request to save PriceRelatedOption : {}", priceRelatedOption);
        return priceRelatedOptionRepository.save(priceRelatedOption);
    }

    /**
     * Update a priceRelatedOption.
     *
     * @param priceRelatedOption the entity to save.
     * @return the persisted entity.
     */
    public PriceRelatedOption update(PriceRelatedOption priceRelatedOption) {
        log.debug("Request to update PriceRelatedOption : {}", priceRelatedOption);
        return priceRelatedOptionRepository.save(priceRelatedOption);
    }

    /**
     * Partially update a priceRelatedOption.
     *
     * @param priceRelatedOption the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PriceRelatedOption> partialUpdate(PriceRelatedOption priceRelatedOption) {
        log.debug("Request to partially update PriceRelatedOption : {}", priceRelatedOption);

        return priceRelatedOptionRepository
            .findById(priceRelatedOption.getId())
            .map(existingPriceRelatedOption -> {
                if (priceRelatedOption.getCode() != null) {
                    existingPriceRelatedOption.setCode(priceRelatedOption.getCode());
                }
                if (priceRelatedOption.getName() != null) {
                    existingPriceRelatedOption.setName(priceRelatedOption.getName());
                }
                if (priceRelatedOption.getIsActive() != null) {
                    existingPriceRelatedOption.setIsActive(priceRelatedOption.getIsActive());
                }

                return existingPriceRelatedOption;
            })
            .map(priceRelatedOptionRepository::save);
    }

    /**
     * Get all the priceRelatedOptions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PriceRelatedOption> findAll(Pageable pageable) {
        log.debug("Request to get all PriceRelatedOptions");
        return priceRelatedOptionRepository.findAll(pageable);
    }

    /**
     * Get all the priceRelatedOptions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PriceRelatedOption> findAllWithEagerRelationships(Pageable pageable) {
        return priceRelatedOptionRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one priceRelatedOption by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PriceRelatedOption> findOne(Long id) {
        log.debug("Request to get PriceRelatedOption : {}", id);
        return priceRelatedOptionRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the priceRelatedOption by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PriceRelatedOption : {}", id);
        priceRelatedOptionRepository.deleteById(id);
    }
}
