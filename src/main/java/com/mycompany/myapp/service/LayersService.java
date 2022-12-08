package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Layers;
import com.mycompany.myapp.repository.LayersRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Layers}.
 */
@Service
@Transactional
public class LayersService {

    private final Logger log = LoggerFactory.getLogger(LayersService.class);

    private final LayersRepository layersRepository;

    public LayersService(LayersRepository layersRepository) {
        this.layersRepository = layersRepository;
    }

    /**
     * Save a layers.
     *
     * @param layers the entity to save.
     * @return the persisted entity.
     */
    public Layers save(Layers layers) {
        log.debug("Request to save Layers : {}", layers);
        return layersRepository.save(layers);
    }

    /**
     * Update a layers.
     *
     * @param layers the entity to save.
     * @return the persisted entity.
     */
    public Layers update(Layers layers) {
        log.debug("Request to update Layers : {}", layers);
        return layersRepository.save(layers);
    }

    /**
     * Partially update a layers.
     *
     * @param layers the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Layers> partialUpdate(Layers layers) {
        log.debug("Request to partially update Layers : {}", layers);

        return layersRepository
            .findById(layers.getId())
            .map(existingLayers -> {
                if (layers.getLayerNo() != null) {
                    existingLayers.setLayerNo(layers.getLayerNo());
                }
                if (layers.getIsActive() != null) {
                    existingLayers.setIsActive(layers.getIsActive());
                }

                return existingLayers;
            })
            .map(layersRepository::save);
    }

    /**
     * Get all the layers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Layers> findAll(Pageable pageable) {
        log.debug("Request to get all Layers");
        return layersRepository.findAll(pageable);
    }

    /**
     * Get all the layers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Layers> findAllWithEagerRelationships(Pageable pageable) {
        return layersRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one layers by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Layers> findOne(Long id) {
        log.debug("Request to get Layers : {}", id);
        return layersRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the layers by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Layers : {}", id);
        layersRepository.deleteById(id);
    }
}
