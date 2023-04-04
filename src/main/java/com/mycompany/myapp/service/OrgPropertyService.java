package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.OrgProperty;
import com.mycompany.myapp.repository.OrgPropertyRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrgProperty}.
 */
@Service
@Transactional
public class OrgPropertyService {

    private final Logger log = LoggerFactory.getLogger(OrgPropertyService.class);

    private final OrgPropertyRepository orgPropertyRepository;

    public OrgPropertyService(OrgPropertyRepository orgPropertyRepository) {
        this.orgPropertyRepository = orgPropertyRepository;
    }

    /**
     * Save a orgProperty.
     *
     * @param orgProperty the entity to save.
     * @return the persisted entity.
     */
    public OrgProperty save(OrgProperty orgProperty) {
        log.debug("Request to save OrgProperty : {}", orgProperty);
        return orgPropertyRepository.save(orgProperty);
    }

    /**
     * Update a orgProperty.
     *
     * @param orgProperty the entity to save.
     * @return the persisted entity.
     */
    public OrgProperty update(OrgProperty orgProperty) {
        log.debug("Request to update OrgProperty : {}", orgProperty);
        return orgPropertyRepository.save(orgProperty);
    }

    /**
     * Partially update a orgProperty.
     *
     * @param orgProperty the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrgProperty> partialUpdate(OrgProperty orgProperty) {
        log.debug("Request to partially update OrgProperty : {}", orgProperty);

        return orgPropertyRepository
            .findById(orgProperty.getId())
            .map(existingOrgProperty -> {
                if (orgProperty.getName() != null) {
                    existingOrgProperty.setName(orgProperty.getName());
                }
                if (orgProperty.getDescription() != null) {
                    existingOrgProperty.setDescription(orgProperty.getDescription());
                }
                if (orgProperty.getIsActive() != null) {
                    existingOrgProperty.setIsActive(orgProperty.getIsActive());
                }

                return existingOrgProperty;
            })
            .map(orgPropertyRepository::save);
    }

    /**
     * Get all the orgProperties.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<OrgProperty> findAll() {
        log.debug("Request to get all OrgProperties");
        return orgPropertyRepository.findAll();
    }

    /**
     * Get one orgProperty by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrgProperty> findOne(Long id) {
        log.debug("Request to get OrgProperty : {}", id);
        return orgPropertyRepository.findById(id);
    }

    /**
     * Delete the orgProperty by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete OrgProperty : {}", id);
        orgPropertyRepository.deleteById(id);
    }

    public Optional<OrgProperty> findOneByName(String name) {
        return orgPropertyRepository.findOneByName(name);
    }
}
