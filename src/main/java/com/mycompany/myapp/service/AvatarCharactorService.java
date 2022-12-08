package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.AvatarCharactor;
import com.mycompany.myapp.repository.AvatarCharactorRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AvatarCharactor}.
 */
@Service
@Transactional
public class AvatarCharactorService {

    private final Logger log = LoggerFactory.getLogger(AvatarCharactorService.class);

    private final AvatarCharactorRepository avatarCharactorRepository;

    public AvatarCharactorService(AvatarCharactorRepository avatarCharactorRepository) {
        this.avatarCharactorRepository = avatarCharactorRepository;
    }

    /**
     * Save a avatarCharactor.
     *
     * @param avatarCharactor the entity to save.
     * @return the persisted entity.
     */
    public AvatarCharactor save(AvatarCharactor avatarCharactor) {
        log.debug("Request to save AvatarCharactor : {}", avatarCharactor);
        return avatarCharactorRepository.save(avatarCharactor);
    }

    /**
     * Update a avatarCharactor.
     *
     * @param avatarCharactor the entity to save.
     * @return the persisted entity.
     */
    public AvatarCharactor update(AvatarCharactor avatarCharactor) {
        log.debug("Request to update AvatarCharactor : {}", avatarCharactor);
        return avatarCharactorRepository.save(avatarCharactor);
    }

    /**
     * Partially update a avatarCharactor.
     *
     * @param avatarCharactor the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AvatarCharactor> partialUpdate(AvatarCharactor avatarCharactor) {
        log.debug("Request to partially update AvatarCharactor : {}", avatarCharactor);

        return avatarCharactorRepository
            .findById(avatarCharactor.getId())
            .map(existingAvatarCharactor -> {
                if (avatarCharactor.getCode() != null) {
                    existingAvatarCharactor.setCode(avatarCharactor.getCode());
                }
                if (avatarCharactor.getDescription() != null) {
                    existingAvatarCharactor.setDescription(avatarCharactor.getDescription());
                }
                if (avatarCharactor.getIsActive() != null) {
                    existingAvatarCharactor.setIsActive(avatarCharactor.getIsActive());
                }

                return existingAvatarCharactor;
            })
            .map(avatarCharactorRepository::save);
    }

    /**
     * Get all the avatarCharactors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AvatarCharactor> findAll(Pageable pageable) {
        log.debug("Request to get all AvatarCharactors");
        return avatarCharactorRepository.findAll(pageable);
    }

    /**
     * Get one avatarCharactor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AvatarCharactor> findOne(Long id) {
        log.debug("Request to get AvatarCharactor : {}", id);
        return avatarCharactorRepository.findById(id);
    }

    /**
     * Delete the avatarCharactor by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AvatarCharactor : {}", id);
        avatarCharactorRepository.deleteById(id);
    }
}
