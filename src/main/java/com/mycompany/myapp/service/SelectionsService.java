package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Selections;
import com.mycompany.myapp.repository.SelectionsRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Selections}.
 */
@Service
@Transactional
public class SelectionsService {

    private final Logger log = LoggerFactory.getLogger(SelectionsService.class);

    private final SelectionsRepository selectionsRepository;

    public SelectionsService(SelectionsRepository selectionsRepository) {
        this.selectionsRepository = selectionsRepository;
    }

    /**
     * Save a selections.
     *
     * @param selections the entity to save.
     * @return the persisted entity.
     */
    public Selections save(Selections selections) {
        log.debug("Request to save Selections : {}", selections);
        return selectionsRepository.save(selections);
    }

    /**
     * Update a selections.
     *
     * @param selections the entity to save.
     * @return the persisted entity.
     */
    public Selections update(Selections selections) {
        log.debug("Request to update Selections : {}", selections);
        return selectionsRepository.save(selections);
    }

    /**
     * Partially update a selections.
     *
     * @param selections the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Selections> partialUpdate(Selections selections) {
        log.debug("Request to partially update Selections : {}", selections);

        return selectionsRepository
            .findById(selections.getId())
            .map(existingSelections -> {
                if (selections.getAvatarCode() != null) {
                    existingSelections.setAvatarCode(selections.getAvatarCode());
                }
                if (selections.getStyleCode() != null) {
                    existingSelections.setStyleCode(selections.getStyleCode());
                }
                if (selections.getOptionCode() != null) {
                    existingSelections.setOptionCode(selections.getOptionCode());
                }
                if (selections.getImage() != null) {
                    existingSelections.setImage(selections.getImage());
                }
                if (selections.getHeight() != null) {
                    existingSelections.setHeight(selections.getHeight());
                }
                if (selections.getX() != null) {
                    existingSelections.setX(selections.getX());
                }
                if (selections.getY() != null) {
                    existingSelections.setY(selections.getY());
                }
                if (selections.getIsActive() != null) {
                    existingSelections.setIsActive(selections.getIsActive());
                }
                if (selections.getWidth() != null) {
                    existingSelections.setWidth(selections.getWidth());
                }
                if (selections.getAvatarAttributesCode() != null) {
                    existingSelections.setAvatarAttributesCode(selections.getAvatarAttributesCode());
                }

                return existingSelections;
            })
            .map(selectionsRepository::save);
    }

    /**
     * Get all the selections.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Selections> findAll() {
        log.debug("Request to get all Selections");
        return selectionsRepository.findAll();
    }

    /**
     * Get one selections by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Selections> findOne(Long id) {
        log.debug("Request to get Selections : {}", id);
        return selectionsRepository.findById(id);
    }

    /**
     * Delete the selections by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Selections : {}", id);
        selectionsRepository.deleteById(id);
    }
}
