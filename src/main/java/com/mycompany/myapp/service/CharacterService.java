package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Character;
import com.mycompany.myapp.repository.CharacterRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Character}.
 */
@Service
@Transactional
public class CharacterService {

    private final Logger log = LoggerFactory.getLogger(CharacterService.class);

    private final CharacterRepository characterRepository;

    public CharacterService(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
    }

    /**
     * Save a character.
     *
     * @param character the entity to save.
     * @return the persisted entity.
     */
    public Character save(Character character) {
        log.debug("Request to save Character : {}", character);
        return characterRepository.save(character);
    }

    /**
     * Update a character.
     *
     * @param character the entity to save.
     * @return the persisted entity.
     */
    public Character update(Character character) {
        log.debug("Request to update Character : {}", character);
        return characterRepository.save(character);
    }

    /**
     * Partially update a character.
     *
     * @param character the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Character> partialUpdate(Character character) {
        log.debug("Request to partially update Character : {}", character);

        return characterRepository
            .findById(character.getId())
            .map(existingCharacter -> {
                if (character.getCode() != null) {
                    existingCharacter.setCode(character.getCode());
                }
                if (character.getDescription() != null) {
                    existingCharacter.setDescription(character.getDescription());
                }
                if (character.getIsActive() != null) {
                    existingCharacter.setIsActive(character.getIsActive());
                }

                return existingCharacter;
            })
            .map(characterRepository::save);
    }

    /**
     * Get all the characters.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Character> findAll() {
        log.debug("Request to get all Characters");
        return characterRepository.findAll();
    }

    /**
     * Get all the characters with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Character> findAllWithEagerRelationships(Pageable pageable) {
        return characterRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one character by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Character> findOne(Long id) {
        log.debug("Request to get Character : {}", id);
        return characterRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the character by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Character : {}", id);
        characterRepository.deleteById(id);
    }
}
