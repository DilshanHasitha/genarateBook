package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.BooksVariables;
import com.mycompany.myapp.repository.BooksVariablesRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BooksVariables}.
 */
@Service
@Transactional
public class BooksVariablesService {

    private final Logger log = LoggerFactory.getLogger(BooksVariablesService.class);

    private final BooksVariablesRepository booksVariablesRepository;

    public BooksVariablesService(BooksVariablesRepository booksVariablesRepository) {
        this.booksVariablesRepository = booksVariablesRepository;
    }

    /**
     * Save a booksVariables.
     *
     * @param booksVariables the entity to save.
     * @return the persisted entity.
     */
    public BooksVariables save(BooksVariables booksVariables) {
        log.debug("Request to save BooksVariables : {}", booksVariables);
        return booksVariablesRepository.save(booksVariables);
    }

    /**
     * Update a booksVariables.
     *
     * @param booksVariables the entity to save.
     * @return the persisted entity.
     */
    public BooksVariables update(BooksVariables booksVariables) {
        log.debug("Request to update BooksVariables : {}", booksVariables);
        return booksVariablesRepository.save(booksVariables);
    }

    /**
     * Partially update a booksVariables.
     *
     * @param booksVariables the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BooksVariables> partialUpdate(BooksVariables booksVariables) {
        log.debug("Request to partially update BooksVariables : {}", booksVariables);

        return booksVariablesRepository
            .findById(booksVariables.getId())
            .map(existingBooksVariables -> {
                if (booksVariables.getCode() != null) {
                    existingBooksVariables.setCode(booksVariables.getCode());
                }
                if (booksVariables.getDescription() != null) {
                    existingBooksVariables.setDescription(booksVariables.getDescription());
                }
                if (booksVariables.getIsActive() != null) {
                    existingBooksVariables.setIsActive(booksVariables.getIsActive());
                }

                return existingBooksVariables;
            })
            .map(booksVariablesRepository::save);
    }

    /**
     * Get all the booksVariables.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BooksVariables> findAll(Pageable pageable) {
        log.debug("Request to get all BooksVariables");
        return booksVariablesRepository.findAll(pageable);
    }

    /**
     * Get one booksVariables by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BooksVariables> findOne(Long id) {
        log.debug("Request to get BooksVariables : {}", id);
        return booksVariablesRepository.findById(id);
    }

    /**
     * Delete the booksVariables by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BooksVariables : {}", id);
        booksVariablesRepository.deleteById(id);
    }
}
