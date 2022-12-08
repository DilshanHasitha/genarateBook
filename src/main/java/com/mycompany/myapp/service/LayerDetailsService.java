package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.LayerDetails;
import com.mycompany.myapp.repository.LayerDetailsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LayerDetails}.
 */
@Service
@Transactional
public class LayerDetailsService {

    private final Logger log = LoggerFactory.getLogger(LayerDetailsService.class);

    private final LayerDetailsRepository layerDetailsRepository;

    public LayerDetailsService(LayerDetailsRepository layerDetailsRepository) {
        this.layerDetailsRepository = layerDetailsRepository;
    }

    /**
     * Save a layerDetails.
     *
     * @param layerDetails the entity to save.
     * @return the persisted entity.
     */
    public LayerDetails save(LayerDetails layerDetails) {
        log.debug("Request to save LayerDetails : {}", layerDetails);
        return layerDetailsRepository.save(layerDetails);
    }

    /**
     * Update a layerDetails.
     *
     * @param layerDetails the entity to save.
     * @return the persisted entity.
     */
    public LayerDetails update(LayerDetails layerDetails) {
        log.debug("Request to update LayerDetails : {}", layerDetails);
        return layerDetailsRepository.save(layerDetails);
    }

    /**
     * Partially update a layerDetails.
     *
     * @param layerDetails the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LayerDetails> partialUpdate(LayerDetails layerDetails) {
        log.debug("Request to partially update LayerDetails : {}", layerDetails);

        return layerDetailsRepository
            .findById(layerDetails.getId())
            .map(existingLayerDetails -> {
                if (layerDetails.getName() != null) {
                    existingLayerDetails.setName(layerDetails.getName());
                }
                if (layerDetails.getDescription() != null) {
                    existingLayerDetails.setDescription(layerDetails.getDescription());
                }

                return existingLayerDetails;
            })
            .map(layerDetailsRepository::save);
    }

    /**
     * Get all the layerDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LayerDetails> findAll(Pageable pageable) {
        log.debug("Request to get all LayerDetails");
        return layerDetailsRepository.findAll(pageable);
    }

    /**
     * Get one layerDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LayerDetails> findOne(Long id) {
        log.debug("Request to get LayerDetails : {}", id);
        return layerDetailsRepository.findById(id);
    }

    /**
     * Delete the layerDetails by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LayerDetails : {}", id);
        layerDetailsRepository.deleteById(id);
    }
}
