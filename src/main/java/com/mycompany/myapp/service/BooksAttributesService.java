package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.BooksAttributes;
import com.mycompany.myapp.repository.BooksAttributesRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BooksAttributes}.
 */
@Service
@Transactional
public class BooksAttributesService {

    private final Logger log = LoggerFactory.getLogger(BooksAttributesService.class);

    private final BooksAttributesRepository booksAttributesRepository;

    public BooksAttributesService(BooksAttributesRepository booksAttributesRepository) {
        this.booksAttributesRepository = booksAttributesRepository;
    }

    /**
     * Save a booksAttributes.
     *
     * @param booksAttributes the entity to save.
     * @return the persisted entity.
     */
    public BooksAttributes save(BooksAttributes booksAttributes) {
        log.debug("Request to save BooksAttributes : {}", booksAttributes);
        return booksAttributesRepository.save(booksAttributes);
    }

    /**
     * Update a booksAttributes.
     *
     * @param booksAttributes the entity to save.
     * @return the persisted entity.
     */
    public BooksAttributes update(BooksAttributes booksAttributes) {
        log.debug("Request to update BooksAttributes : {}", booksAttributes);
        return booksAttributesRepository.save(booksAttributes);
    }

    /**
     * Partially update a booksAttributes.
     *
     * @param booksAttributes the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BooksAttributes> partialUpdate(BooksAttributes booksAttributes) {
        log.debug("Request to partially update BooksAttributes : {}", booksAttributes);

        return booksAttributesRepository
            .findById(booksAttributes.getId())
            .map(existingBooksAttributes -> {
                if (booksAttributes.getCode() != null) {
                    existingBooksAttributes.setCode(booksAttributes.getCode());
                }
                if (booksAttributes.getDescription() != null) {
                    existingBooksAttributes.setDescription(booksAttributes.getDescription());
                }
                if (booksAttributes.getIsActive() != null) {
                    existingBooksAttributes.setIsActive(booksAttributes.getIsActive());
                }

                return existingBooksAttributes;
            })
            .map(booksAttributesRepository::save);
    }

    /**
     * Get all the booksAttributes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BooksAttributes> findAll(Pageable pageable) {
        log.debug("Request to get all BooksAttributes");
        return booksAttributesRepository.findAll(pageable);
    }

    /**
     * Get one booksAttributes by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BooksAttributes> findOne(Long id) {
        log.debug("Request to get BooksAttributes : {}", id);
        return booksAttributesRepository.findById(id);
    }

    /**
     * Delete the booksAttributes by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BooksAttributes : {}", id);
        booksAttributesRepository.deleteById(id);
    }
}
