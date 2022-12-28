package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.FontFamily;
import com.mycompany.myapp.repository.FontFamilyRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FontFamily}.
 */
@Service
@Transactional
public class FontFamilyService {

    private final Logger log = LoggerFactory.getLogger(FontFamilyService.class);

    private final FontFamilyRepository fontFamilyRepository;

    public FontFamilyService(FontFamilyRepository fontFamilyRepository) {
        this.fontFamilyRepository = fontFamilyRepository;
    }

    /**
     * Save a fontFamily.
     *
     * @param fontFamily the entity to save.
     * @return the persisted entity.
     */
    public FontFamily save(FontFamily fontFamily) {
        log.debug("Request to save FontFamily : {}", fontFamily);
        return fontFamilyRepository.save(fontFamily);
    }

    /**
     * Update a fontFamily.
     *
     * @param fontFamily the entity to save.
     * @return the persisted entity.
     */
    public FontFamily update(FontFamily fontFamily) {
        log.debug("Request to update FontFamily : {}", fontFamily);
        return fontFamilyRepository.save(fontFamily);
    }

    /**
     * Partially update a fontFamily.
     *
     * @param fontFamily the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FontFamily> partialUpdate(FontFamily fontFamily) {
        log.debug("Request to partially update FontFamily : {}", fontFamily);

        return fontFamilyRepository
            .findById(fontFamily.getId())
            .map(existingFontFamily -> {
                if (fontFamily.getName() != null) {
                    existingFontFamily.setName(fontFamily.getName());
                }
                if (fontFamily.getUrl() != null) {
                    existingFontFamily.setUrl(fontFamily.getUrl());
                }
                if (fontFamily.getIsActive() != null) {
                    existingFontFamily.setIsActive(fontFamily.getIsActive());
                }

                return existingFontFamily;
            })
            .map(fontFamilyRepository::save);
    }

    /**
     * Get all the fontFamilies.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<FontFamily> findAll() {
        log.debug("Request to get all FontFamilies");
        return fontFamilyRepository.findAll();
    }

    /**
     * Get one fontFamily by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FontFamily> findOne(Long id) {
        log.debug("Request to get FontFamily : {}", id);
        return fontFamilyRepository.findById(id);
    }

    /**
     * Delete the fontFamily by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete FontFamily : {}", id);
        fontFamilyRepository.deleteById(id);
    }
}
