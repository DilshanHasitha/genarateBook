package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Styles;
import com.mycompany.myapp.repository.StylesRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Styles}.
 */
@Service
@Transactional
public class StylesService {

    private final Logger log = LoggerFactory.getLogger(StylesService.class);

    private final StylesRepository stylesRepository;

    public StylesService(StylesRepository stylesRepository) {
        this.stylesRepository = stylesRepository;
    }

    /**
     * Save a styles.
     *
     * @param styles the entity to save.
     * @return the persisted entity.
     */
    public Styles save(Styles styles) {
        log.debug("Request to save Styles : {}", styles);
        return stylesRepository.save(styles);
    }

    /**
     * Update a styles.
     *
     * @param styles the entity to save.
     * @return the persisted entity.
     */
    public Styles update(Styles styles) {
        log.debug("Request to update Styles : {}", styles);
        return stylesRepository.save(styles);
    }

    /**
     * Partially update a styles.
     *
     * @param styles the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Styles> partialUpdate(Styles styles) {
        log.debug("Request to partially update Styles : {}", styles);

        return stylesRepository
            .findById(styles.getId())
            .map(existingStyles -> {
                if (styles.getCode() != null) {
                    existingStyles.setCode(styles.getCode());
                }
                if (styles.getDescription() != null) {
                    existingStyles.setDescription(styles.getDescription());
                }
                if (styles.getImgURL() != null) {
                    existingStyles.setImgURL(styles.getImgURL());
                }
                if (styles.getIsActive() != null) {
                    existingStyles.setIsActive(styles.getIsActive());
                }

                return existingStyles;
            })
            .map(stylesRepository::save);
    }

    /**
     * Get all the styles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Styles> findAll(Pageable pageable) {
        log.debug("Request to get all Styles");
        return stylesRepository.findAll(pageable);
    }

    /**
     * Get one styles by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Styles> findOne(Long id) {
        log.debug("Request to get Styles : {}", id);
        return stylesRepository.findById(id);
    }

    /**
     * Delete the styles by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Styles : {}", id);
        stylesRepository.deleteById(id);
    }
}
