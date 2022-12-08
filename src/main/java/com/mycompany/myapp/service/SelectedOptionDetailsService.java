package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.SelectedOptionDetails;
import com.mycompany.myapp.repository.SelectedOptionDetailsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SelectedOptionDetails}.
 */
@Service
@Transactional
public class SelectedOptionDetailsService {

    private final Logger log = LoggerFactory.getLogger(SelectedOptionDetailsService.class);

    private final SelectedOptionDetailsRepository selectedOptionDetailsRepository;

    public SelectedOptionDetailsService(SelectedOptionDetailsRepository selectedOptionDetailsRepository) {
        this.selectedOptionDetailsRepository = selectedOptionDetailsRepository;
    }

    /**
     * Save a selectedOptionDetails.
     *
     * @param selectedOptionDetails the entity to save.
     * @return the persisted entity.
     */
    public SelectedOptionDetails save(SelectedOptionDetails selectedOptionDetails) {
        log.debug("Request to save SelectedOptionDetails : {}", selectedOptionDetails);
        return selectedOptionDetailsRepository.save(selectedOptionDetails);
    }

    /**
     * Update a selectedOptionDetails.
     *
     * @param selectedOptionDetails the entity to save.
     * @return the persisted entity.
     */
    public SelectedOptionDetails update(SelectedOptionDetails selectedOptionDetails) {
        log.debug("Request to update SelectedOptionDetails : {}", selectedOptionDetails);
        return selectedOptionDetailsRepository.save(selectedOptionDetails);
    }

    /**
     * Partially update a selectedOptionDetails.
     *
     * @param selectedOptionDetails the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SelectedOptionDetails> partialUpdate(SelectedOptionDetails selectedOptionDetails) {
        log.debug("Request to partially update SelectedOptionDetails : {}", selectedOptionDetails);

        return selectedOptionDetailsRepository
            .findById(selectedOptionDetails.getId())
            .map(existingSelectedOptionDetails -> {
                if (selectedOptionDetails.getCode() != null) {
                    existingSelectedOptionDetails.setCode(selectedOptionDetails.getCode());
                }
                if (selectedOptionDetails.getName() != null) {
                    existingSelectedOptionDetails.setName(selectedOptionDetails.getName());
                }
                if (selectedOptionDetails.getSelectedValue() != null) {
                    existingSelectedOptionDetails.setSelectedValue(selectedOptionDetails.getSelectedValue());
                }
                if (selectedOptionDetails.getIsActive() != null) {
                    existingSelectedOptionDetails.setIsActive(selectedOptionDetails.getIsActive());
                }

                return existingSelectedOptionDetails;
            })
            .map(selectedOptionDetailsRepository::save);
    }

    /**
     * Get all the selectedOptionDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SelectedOptionDetails> findAll(Pageable pageable) {
        log.debug("Request to get all SelectedOptionDetails");
        return selectedOptionDetailsRepository.findAll(pageable);
    }

    /**
     * Get one selectedOptionDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SelectedOptionDetails> findOne(Long id) {
        log.debug("Request to get SelectedOptionDetails : {}", id);
        return selectedOptionDetailsRepository.findById(id);
    }

    /**
     * Delete the selectedOptionDetails by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SelectedOptionDetails : {}", id);
        selectedOptionDetailsRepository.deleteById(id);
    }
}
