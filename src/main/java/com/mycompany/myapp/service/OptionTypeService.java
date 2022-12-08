package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.OptionType;
import com.mycompany.myapp.repository.OptionTypeRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OptionType}.
 */
@Service
@Transactional
public class OptionTypeService {

    private final Logger log = LoggerFactory.getLogger(OptionTypeService.class);

    private final OptionTypeRepository optionTypeRepository;

    public OptionTypeService(OptionTypeRepository optionTypeRepository) {
        this.optionTypeRepository = optionTypeRepository;
    }

    /**
     * Save a optionType.
     *
     * @param optionType the entity to save.
     * @return the persisted entity.
     */
    public OptionType save(OptionType optionType) {
        log.debug("Request to save OptionType : {}", optionType);
        return optionTypeRepository.save(optionType);
    }

    /**
     * Update a optionType.
     *
     * @param optionType the entity to save.
     * @return the persisted entity.
     */
    public OptionType update(OptionType optionType) {
        log.debug("Request to update OptionType : {}", optionType);
        return optionTypeRepository.save(optionType);
    }

    /**
     * Partially update a optionType.
     *
     * @param optionType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OptionType> partialUpdate(OptionType optionType) {
        log.debug("Request to partially update OptionType : {}", optionType);

        return optionTypeRepository
            .findById(optionType.getId())
            .map(existingOptionType -> {
                if (optionType.getCode() != null) {
                    existingOptionType.setCode(optionType.getCode());
                }
                if (optionType.getDescription() != null) {
                    existingOptionType.setDescription(optionType.getDescription());
                }
                if (optionType.getIsActive() != null) {
                    existingOptionType.setIsActive(optionType.getIsActive());
                }

                return existingOptionType;
            })
            .map(optionTypeRepository::save);
    }

    /**
     * Get all the optionTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OptionType> findAll(Pageable pageable) {
        log.debug("Request to get all OptionTypes");
        return optionTypeRepository.findAll(pageable);
    }

    /**
     * Get one optionType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OptionType> findOne(Long id) {
        log.debug("Request to get OptionType : {}", id);
        return optionTypeRepository.findById(id);
    }

    /**
     * Delete the optionType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete OptionType : {}", id);
        optionTypeRepository.deleteById(id);
    }
}
