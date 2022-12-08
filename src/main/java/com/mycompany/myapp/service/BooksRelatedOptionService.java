package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.BooksRelatedOption;
import com.mycompany.myapp.repository.BooksRelatedOptionRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BooksRelatedOption}.
 */
@Service
@Transactional
public class BooksRelatedOptionService {

    private final Logger log = LoggerFactory.getLogger(BooksRelatedOptionService.class);

    private final BooksRelatedOptionRepository booksRelatedOptionRepository;

    public BooksRelatedOptionService(BooksRelatedOptionRepository booksRelatedOptionRepository) {
        this.booksRelatedOptionRepository = booksRelatedOptionRepository;
    }

    /**
     * Save a booksRelatedOption.
     *
     * @param booksRelatedOption the entity to save.
     * @return the persisted entity.
     */
    public BooksRelatedOption save(BooksRelatedOption booksRelatedOption) {
        log.debug("Request to save BooksRelatedOption : {}", booksRelatedOption);
        return booksRelatedOptionRepository.save(booksRelatedOption);
    }

    /**
     * Update a booksRelatedOption.
     *
     * @param booksRelatedOption the entity to save.
     * @return the persisted entity.
     */
    public BooksRelatedOption update(BooksRelatedOption booksRelatedOption) {
        log.debug("Request to update BooksRelatedOption : {}", booksRelatedOption);
        return booksRelatedOptionRepository.save(booksRelatedOption);
    }

    /**
     * Partially update a booksRelatedOption.
     *
     * @param booksRelatedOption the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BooksRelatedOption> partialUpdate(BooksRelatedOption booksRelatedOption) {
        log.debug("Request to partially update BooksRelatedOption : {}", booksRelatedOption);

        return booksRelatedOptionRepository
            .findById(booksRelatedOption.getId())
            .map(existingBooksRelatedOption -> {
                if (booksRelatedOption.getCode() != null) {
                    existingBooksRelatedOption.setCode(booksRelatedOption.getCode());
                }
                if (booksRelatedOption.getName() != null) {
                    existingBooksRelatedOption.setName(booksRelatedOption.getName());
                }
                if (booksRelatedOption.getIsActive() != null) {
                    existingBooksRelatedOption.setIsActive(booksRelatedOption.getIsActive());
                }

                return existingBooksRelatedOption;
            })
            .map(booksRelatedOptionRepository::save);
    }

    /**
     * Get all the booksRelatedOptions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BooksRelatedOption> findAll(Pageable pageable) {
        log.debug("Request to get all BooksRelatedOptions");
        return booksRelatedOptionRepository.findAll(pageable);
    }

    /**
     * Get all the booksRelatedOptions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<BooksRelatedOption> findAllWithEagerRelationships(Pageable pageable) {
        return booksRelatedOptionRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one booksRelatedOption by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BooksRelatedOption> findOne(Long id) {
        log.debug("Request to get BooksRelatedOption : {}", id);
        return booksRelatedOptionRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the booksRelatedOption by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BooksRelatedOption : {}", id);
        booksRelatedOptionRepository.deleteById(id);
    }
}
