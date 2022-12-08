package com.mycompany.myapp.service;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

import com.mycompany.myapp.domain.AvatarAttributes;
import com.mycompany.myapp.domain.AvatarCharactor;
import com.mycompany.myapp.domain.Options;
import com.mycompany.myapp.domain.Styles;
import com.mycompany.myapp.repository.AvatarAttributesRepository;
import com.mycompany.myapp.repository.StylesRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AvatarAttributes}.
 */
@Service
@Transactional
public class AvatarAttributesService {

    private final Logger log = LoggerFactory.getLogger(AvatarAttributesService.class);

    private final AvatarAttributesRepository avatarAttributesRepository;
    private final AvatarCharactorService avatarCharactorService;
    private final StylesService stylesService;

    private final OptionsService optionsService;
    private final StylesRepository stylesRepository;

    public AvatarAttributesService(
        AvatarAttributesRepository avatarAttributesRepository,
        AvatarCharactorService avatarCharactorService,
        StylesService stylesService,
        OptionsService optionsService,
        StylesRepository stylesRepository
    ) {
        this.avatarAttributesRepository = avatarAttributesRepository;
        this.avatarCharactorService = avatarCharactorService;
        this.stylesService = stylesService;
        this.optionsService = optionsService;
        this.stylesRepository = stylesRepository;
    }

    /**
     * Save a avatarAttributes.
     *
     * @param avatarAttributes the entity to save.
     * @return the persisted entity.
     */
    public AvatarAttributes save(AvatarAttributes avatarAttributes) {
        log.debug("Request to save AvatarAttributes : {}", avatarAttributes);
        for (AvatarCharactor avatarcharactor : avatarAttributes.getAvatarCharactors()) {
            if (avatarcharactor.getId() == null) {
                avatarCharactorService.save(avatarcharactor);
            }
        }

        for (Styles styles : avatarAttributes.getStyles()) {
            for (Options options : styles.getOptions()) {
                if (options.getId() == null) {
                    optionsService.save(options);
                }
            }
            if (styles.getId() == null) {
                stylesService.save(styles);
            } else {
                if (!Objects.equals(styles.getId(), styles.getId())) {
                    throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
                }

                if (!stylesRepository.existsById(styles.getId())) {
                    throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
                }

                Styles result = stylesService.update(styles);
            }
        }
        //        if(avatarAttributes.get)
        return avatarAttributesRepository.save(avatarAttributes);
    }

    /**
     * Update a avatarAttributes.
     *
     * @param avatarAttributes the entity to save.
     * @return the persisted entity.
     */
    public AvatarAttributes update(AvatarAttributes avatarAttributes) {
        log.debug("Request to update AvatarAttributes : {}", avatarAttributes);
        return avatarAttributesRepository.save(avatarAttributes);
    }

    /**
     * Partially update a avatarAttributes.
     *
     * @param avatarAttributes the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AvatarAttributes> partialUpdate(AvatarAttributes avatarAttributes) {
        log.debug("Request to partially update AvatarAttributes : {}", avatarAttributes);

        return avatarAttributesRepository
            .findById(avatarAttributes.getId())
            .map(existingAvatarAttributes -> {
                if (avatarAttributes.getCode() != null) {
                    existingAvatarAttributes.setCode(avatarAttributes.getCode());
                }
                if (avatarAttributes.getDescription() != null) {
                    existingAvatarAttributes.setDescription(avatarAttributes.getDescription());
                }
                if (avatarAttributes.getIsActive() != null) {
                    existingAvatarAttributes.setIsActive(avatarAttributes.getIsActive());
                }

                return existingAvatarAttributes;
            })
            .map(avatarAttributesRepository::save);
    }

    /**
     * Get all the avatarAttributes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AvatarAttributes> findAll(Pageable pageable) {
        log.debug("Request to get all AvatarAttributes");
        return avatarAttributesRepository.findAll(pageable);
    }

    /**
     * Get all the avatarAttributes with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<AvatarAttributes> findAllWithEagerRelationships(Pageable pageable) {
        return avatarAttributesRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one avatarAttributes by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AvatarAttributes> findOne(Long id) {
        log.debug("Request to get AvatarAttributes : {}", id);
        return avatarAttributesRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the avatarAttributes by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AvatarAttributes : {}", id);
        avatarAttributesRepository.deleteById(id);
    }
}
