package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.ImageStoreType;
import com.mycompany.myapp.repository.ImageStoreTypeRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ImageStoreType}.
 */
@Service
@Transactional
public class ImageStoreTypeService {

    private final Logger log = LoggerFactory.getLogger(ImageStoreTypeService.class);

    private final ImageStoreTypeRepository imageStoreTypeRepository;

    public ImageStoreTypeService(ImageStoreTypeRepository imageStoreTypeRepository) {
        this.imageStoreTypeRepository = imageStoreTypeRepository;
    }

    /**
     * Save a imageStoreType.
     *
     * @param imageStoreType the entity to save.
     * @return the persisted entity.
     */
    public ImageStoreType save(ImageStoreType imageStoreType) {
        log.debug("Request to save ImageStoreType : {}", imageStoreType);
        return imageStoreTypeRepository.save(imageStoreType);
    }

    /**
     * Update a imageStoreType.
     *
     * @param imageStoreType the entity to save.
     * @return the persisted entity.
     */
    public ImageStoreType update(ImageStoreType imageStoreType) {
        log.debug("Request to update ImageStoreType : {}", imageStoreType);
        return imageStoreTypeRepository.save(imageStoreType);
    }

    /**
     * Partially update a imageStoreType.
     *
     * @param imageStoreType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ImageStoreType> partialUpdate(ImageStoreType imageStoreType) {
        log.debug("Request to partially update ImageStoreType : {}", imageStoreType);

        return imageStoreTypeRepository
            .findById(imageStoreType.getId())
            .map(existingImageStoreType -> {
                if (imageStoreType.getImageStoreTypeCode() != null) {
                    existingImageStoreType.setImageStoreTypeCode(imageStoreType.getImageStoreTypeCode());
                }
                if (imageStoreType.getImageStoreTypeDescription() != null) {
                    existingImageStoreType.setImageStoreTypeDescription(imageStoreType.getImageStoreTypeDescription());
                }
                if (imageStoreType.getIsActive() != null) {
                    existingImageStoreType.setIsActive(imageStoreType.getIsActive());
                }

                return existingImageStoreType;
            })
            .map(imageStoreTypeRepository::save);
    }

    /**
     * Get all the imageStoreTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ImageStoreType> findAll(Pageable pageable) {
        log.debug("Request to get all ImageStoreTypes");
        return imageStoreTypeRepository.findAll(pageable);
    }

    /**
     * Get one imageStoreType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ImageStoreType> findOne(Long id) {
        log.debug("Request to get ImageStoreType : {}", id);
        return imageStoreTypeRepository.findById(id);
    }

    /**
     * Delete the imageStoreType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ImageStoreType : {}", id);
        imageStoreTypeRepository.deleteById(id);
    }
}
