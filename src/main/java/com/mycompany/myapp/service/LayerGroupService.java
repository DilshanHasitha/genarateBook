package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.LayerGroup;
import com.mycompany.myapp.repository.LayerGroupRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LayerGroup}.
 */
@Service
@Transactional
public class LayerGroupService {

    private final Logger log = LoggerFactory.getLogger(LayerGroupService.class);

    private final LayerGroupRepository layerGroupRepository;

    public LayerGroupService(LayerGroupRepository layerGroupRepository) {
        this.layerGroupRepository = layerGroupRepository;
    }

    /**
     * Save a layerGroup.
     *
     * @param layerGroup the entity to save.
     * @return the persisted entity.
     */
    public LayerGroup save(LayerGroup layerGroup) {
        log.debug("Request to save LayerGroup : {}", layerGroup);
        return layerGroupRepository.save(layerGroup);
    }

    /**
     * Update a layerGroup.
     *
     * @param layerGroup the entity to save.
     * @return the persisted entity.
     */
    public LayerGroup update(LayerGroup layerGroup) {
        log.debug("Request to update LayerGroup : {}", layerGroup);
        return layerGroupRepository.save(layerGroup);
    }

    /**
     * Partially update a layerGroup.
     *
     * @param layerGroup the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LayerGroup> partialUpdate(LayerGroup layerGroup) {
        log.debug("Request to partially update LayerGroup : {}", layerGroup);

        return layerGroupRepository
            .findById(layerGroup.getId())
            .map(existingLayerGroup -> {
                if (layerGroup.getCode() != null) {
                    existingLayerGroup.setCode(layerGroup.getCode());
                }
                if (layerGroup.getDescription() != null) {
                    existingLayerGroup.setDescription(layerGroup.getDescription());
                }
                if (layerGroup.getIsActive() != null) {
                    existingLayerGroup.setIsActive(layerGroup.getIsActive());
                }

                return existingLayerGroup;
            })
            .map(layerGroupRepository::save);
    }

    /**
     * Get all the layerGroups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LayerGroup> findAll(Pageable pageable) {
        log.debug("Request to get all LayerGroups");
        return layerGroupRepository.findAll(pageable);
    }

    /**
     * Get all the layerGroups with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<LayerGroup> findAllWithEagerRelationships(Pageable pageable) {
        return layerGroupRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one layerGroup by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LayerGroup> findOne(Long id) {
        log.debug("Request to get LayerGroup : {}", id);
        return layerGroupRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the layerGroup by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LayerGroup : {}", id);
        layerGroupRepository.deleteById(id);
    }
}
