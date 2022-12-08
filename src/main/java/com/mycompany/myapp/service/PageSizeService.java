package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.PageSize;
import com.mycompany.myapp.repository.PageSizeRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PageSize}.
 */
@Service
@Transactional
public class PageSizeService {

    private final Logger log = LoggerFactory.getLogger(PageSizeService.class);

    private final PageSizeRepository pageSizeRepository;

    public PageSizeService(PageSizeRepository pageSizeRepository) {
        this.pageSizeRepository = pageSizeRepository;
    }

    /**
     * Save a pageSize.
     *
     * @param pageSize the entity to save.
     * @return the persisted entity.
     */
    public PageSize save(PageSize pageSize) {
        log.debug("Request to save PageSize : {}", pageSize);
        return pageSizeRepository.save(pageSize);
    }

    /**
     * Update a pageSize.
     *
     * @param pageSize the entity to save.
     * @return the persisted entity.
     */
    public PageSize update(PageSize pageSize) {
        log.debug("Request to update PageSize : {}", pageSize);
        return pageSizeRepository.save(pageSize);
    }

    /**
     * Partially update a pageSize.
     *
     * @param pageSize the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PageSize> partialUpdate(PageSize pageSize) {
        log.debug("Request to partially update PageSize : {}", pageSize);

        return pageSizeRepository
            .findById(pageSize.getId())
            .map(existingPageSize -> {
                if (pageSize.getCode() != null) {
                    existingPageSize.setCode(pageSize.getCode());
                }
                if (pageSize.getDescription() != null) {
                    existingPageSize.setDescription(pageSize.getDescription());
                }
                if (pageSize.getIsActive() != null) {
                    existingPageSize.setIsActive(pageSize.getIsActive());
                }
                if (pageSize.getWidth() != null) {
                    existingPageSize.setWidth(pageSize.getWidth());
                }
                if (pageSize.getHeight() != null) {
                    existingPageSize.setHeight(pageSize.getHeight());
                }

                return existingPageSize;
            })
            .map(pageSizeRepository::save);
    }

    /**
     * Get all the pageSizes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PageSize> findAll(Pageable pageable) {
        log.debug("Request to get all PageSizes");
        return pageSizeRepository.findAll(pageable);
    }

    /**
     * Get one pageSize by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PageSize> findOne(Long id) {
        log.debug("Request to get PageSize : {}", id);
        return pageSizeRepository.findById(id);
    }

    /**
     * Delete the pageSize by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PageSize : {}", id);
        pageSizeRepository.deleteById(id);
    }
}
