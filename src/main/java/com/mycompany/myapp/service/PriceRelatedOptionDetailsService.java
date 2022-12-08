package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.PriceRelatedOptionDetails;
import com.mycompany.myapp.repository.PriceRelatedOptionDetailsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PriceRelatedOptionDetails}.
 */
@Service
@Transactional
public class PriceRelatedOptionDetailsService {

    private final Logger log = LoggerFactory.getLogger(PriceRelatedOptionDetailsService.class);

    private final PriceRelatedOptionDetailsRepository priceRelatedOptionDetailsRepository;

    public PriceRelatedOptionDetailsService(PriceRelatedOptionDetailsRepository priceRelatedOptionDetailsRepository) {
        this.priceRelatedOptionDetailsRepository = priceRelatedOptionDetailsRepository;
    }

    /**
     * Save a priceRelatedOptionDetails.
     *
     * @param priceRelatedOptionDetails the entity to save.
     * @return the persisted entity.
     */
    public PriceRelatedOptionDetails save(PriceRelatedOptionDetails priceRelatedOptionDetails) {
        log.debug("Request to save PriceRelatedOptionDetails : {}", priceRelatedOptionDetails);
        return priceRelatedOptionDetailsRepository.save(priceRelatedOptionDetails);
    }

    /**
     * Update a priceRelatedOptionDetails.
     *
     * @param priceRelatedOptionDetails the entity to save.
     * @return the persisted entity.
     */
    public PriceRelatedOptionDetails update(PriceRelatedOptionDetails priceRelatedOptionDetails) {
        log.debug("Request to update PriceRelatedOptionDetails : {}", priceRelatedOptionDetails);
        return priceRelatedOptionDetailsRepository.save(priceRelatedOptionDetails);
    }

    /**
     * Partially update a priceRelatedOptionDetails.
     *
     * @param priceRelatedOptionDetails the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PriceRelatedOptionDetails> partialUpdate(PriceRelatedOptionDetails priceRelatedOptionDetails) {
        log.debug("Request to partially update PriceRelatedOptionDetails : {}", priceRelatedOptionDetails);

        return priceRelatedOptionDetailsRepository
            .findById(priceRelatedOptionDetails.getId())
            .map(existingPriceRelatedOptionDetails -> {
                if (priceRelatedOptionDetails.getDescription() != null) {
                    existingPriceRelatedOptionDetails.setDescription(priceRelatedOptionDetails.getDescription());
                }
                if (priceRelatedOptionDetails.getPrice() != null) {
                    existingPriceRelatedOptionDetails.setPrice(priceRelatedOptionDetails.getPrice());
                }

                return existingPriceRelatedOptionDetails;
            })
            .map(priceRelatedOptionDetailsRepository::save);
    }

    /**
     * Get all the priceRelatedOptionDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PriceRelatedOptionDetails> findAll(Pageable pageable) {
        log.debug("Request to get all PriceRelatedOptionDetails");
        return priceRelatedOptionDetailsRepository.findAll(pageable);
    }

    /**
     * Get one priceRelatedOptionDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PriceRelatedOptionDetails> findOne(Long id) {
        log.debug("Request to get PriceRelatedOptionDetails : {}", id);
        return priceRelatedOptionDetailsRepository.findById(id);
    }

    /**
     * Delete the priceRelatedOptionDetails by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PriceRelatedOptionDetails : {}", id);
        priceRelatedOptionDetailsRepository.deleteById(id);
    }
}
