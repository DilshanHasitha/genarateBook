package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.SelectedOption;
import com.mycompany.myapp.repository.SelectedOptionRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SelectedOption}.
 */
@Service
@Transactional
public class SelectedOptionService {

    private final Logger log = LoggerFactory.getLogger(SelectedOptionService.class);

    private final SelectedOptionRepository selectedOptionRepository;

    public SelectedOptionService(SelectedOptionRepository selectedOptionRepository) {
        this.selectedOptionRepository = selectedOptionRepository;
    }

    /**
     * Save a selectedOption.
     *
     * @param selectedOption the entity to save.
     * @return the persisted entity.
     */
    public SelectedOption save(SelectedOption selectedOption) {
        log.debug("Request to save SelectedOption : {}", selectedOption);
        return selectedOptionRepository.save(selectedOption);
    }

    /**
     * Update a selectedOption.
     *
     * @param selectedOption the entity to save.
     * @return the persisted entity.
     */
    public SelectedOption update(SelectedOption selectedOption) {
        log.debug("Request to update SelectedOption : {}", selectedOption);
        return selectedOptionRepository.save(selectedOption);
    }

    /**
     * Partially update a selectedOption.
     *
     * @param selectedOption the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SelectedOption> partialUpdate(SelectedOption selectedOption) {
        log.debug("Request to partially update SelectedOption : {}", selectedOption);

        return selectedOptionRepository
            .findById(selectedOption.getId())
            .map(existingSelectedOption -> {
                if (selectedOption.getCode() != null) {
                    existingSelectedOption.setCode(selectedOption.getCode());
                }
                if (selectedOption.getDate() != null) {
                    existingSelectedOption.setDate(selectedOption.getDate());
                }

                return existingSelectedOption;
            })
            .map(selectedOptionRepository::save);
    }

    /**
     * Get all the selectedOptions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SelectedOption> findAll(Pageable pageable) {
        log.debug("Request to get all SelectedOptions");
        return selectedOptionRepository.findAll(pageable);
    }

    /**
     * Get all the selectedOptions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SelectedOption> findAllWithEagerRelationships(Pageable pageable) {
        return selectedOptionRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one selectedOption by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SelectedOption> findOne(Long id) {
        log.debug("Request to get SelectedOption : {}", id);
        return selectedOptionRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the selectedOption by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SelectedOption : {}", id);
        selectedOptionRepository.deleteById(id);
    }
}
