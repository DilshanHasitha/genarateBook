package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.StylesDetails;
import com.mycompany.myapp.repository.StylesDetailsRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link StylesDetails}.
 */
@Service
@Transactional
public class StylesDetailsService {

    private final Logger log = LoggerFactory.getLogger(StylesDetailsService.class);

    private final StylesDetailsRepository stylesDetailsRepository;

    public StylesDetailsService(StylesDetailsRepository stylesDetailsRepository) {
        this.stylesDetailsRepository = stylesDetailsRepository;
    }

    /**
     * Save a stylesDetails.
     *
     * @param stylesDetails the entity to save.
     * @return the persisted entity.
     */
    public StylesDetails save(StylesDetails stylesDetails) {
        log.debug("Request to save StylesDetails : {}", stylesDetails);
        return stylesDetailsRepository.save(stylesDetails);
    }

    /**
     * Update a stylesDetails.
     *
     * @param stylesDetails the entity to save.
     * @return the persisted entity.
     */
    public StylesDetails update(StylesDetails stylesDetails) {
        log.debug("Request to update StylesDetails : {}", stylesDetails);
        return stylesDetailsRepository.save(stylesDetails);
    }

    /**
     * Partially update a stylesDetails.
     *
     * @param stylesDetails the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<StylesDetails> partialUpdate(StylesDetails stylesDetails) {
        log.debug("Request to partially update StylesDetails : {}", stylesDetails);

        return stylesDetailsRepository
            .findById(stylesDetails.getId())
            .map(existingStylesDetails -> {
                if (stylesDetails.getIsActive() != null) {
                    existingStylesDetails.setIsActive(stylesDetails.getIsActive());
                }
                if (stylesDetails.getTemplateValue() != null) {
                    existingStylesDetails.setTemplateValue(stylesDetails.getTemplateValue());
                }
                if (stylesDetails.getReplaceValue() != null) {
                    existingStylesDetails.setReplaceValue(stylesDetails.getReplaceValue());
                }

                return existingStylesDetails;
            })
            .map(stylesDetailsRepository::save);
    }

    /**
     * Get all the stylesDetails.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<StylesDetails> findAll() {
        log.debug("Request to get all StylesDetails");
        return stylesDetailsRepository.findAll();
    }

    /**
     * Get one stylesDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StylesDetails> findOne(Long id) {
        log.debug("Request to get StylesDetails : {}", id);
        return stylesDetailsRepository.findById(id);
    }

    /**
     * Delete the stylesDetails by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete StylesDetails : {}", id);
        stylesDetailsRepository.deleteById(id);
    }
}
