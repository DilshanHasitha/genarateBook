package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.BooksRelatedOptionDetails;
import com.mycompany.myapp.repository.BooksRelatedOptionDetailsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BooksRelatedOptionDetails}.
 */
@Service
@Transactional
public class BooksRelatedOptionDetailsService {

    private final Logger log = LoggerFactory.getLogger(BooksRelatedOptionDetailsService.class);

    private final BooksRelatedOptionDetailsRepository booksRelatedOptionDetailsRepository;

    public BooksRelatedOptionDetailsService(BooksRelatedOptionDetailsRepository booksRelatedOptionDetailsRepository) {
        this.booksRelatedOptionDetailsRepository = booksRelatedOptionDetailsRepository;
    }

    /**
     * Save a booksRelatedOptionDetails.
     *
     * @param booksRelatedOptionDetails the entity to save.
     * @return the persisted entity.
     */
    public BooksRelatedOptionDetails save(BooksRelatedOptionDetails booksRelatedOptionDetails) {
        log.debug("Request to save BooksRelatedOptionDetails : {}", booksRelatedOptionDetails);
        return booksRelatedOptionDetailsRepository.save(booksRelatedOptionDetails);
    }

    /**
     * Update a booksRelatedOptionDetails.
     *
     * @param booksRelatedOptionDetails the entity to save.
     * @return the persisted entity.
     */
    public BooksRelatedOptionDetails update(BooksRelatedOptionDetails booksRelatedOptionDetails) {
        log.debug("Request to update BooksRelatedOptionDetails : {}", booksRelatedOptionDetails);
        return booksRelatedOptionDetailsRepository.save(booksRelatedOptionDetails);
    }

    /**
     * Partially update a booksRelatedOptionDetails.
     *
     * @param booksRelatedOptionDetails the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BooksRelatedOptionDetails> partialUpdate(BooksRelatedOptionDetails booksRelatedOptionDetails) {
        log.debug("Request to partially update BooksRelatedOptionDetails : {}", booksRelatedOptionDetails);

        return booksRelatedOptionDetailsRepository
            .findById(booksRelatedOptionDetails.getId())
            .map(existingBooksRelatedOptionDetails -> {
                if (booksRelatedOptionDetails.getCode() != null) {
                    existingBooksRelatedOptionDetails.setCode(booksRelatedOptionDetails.getCode());
                }
                if (booksRelatedOptionDetails.getDescription() != null) {
                    existingBooksRelatedOptionDetails.setDescription(booksRelatedOptionDetails.getDescription());
                }
                if (booksRelatedOptionDetails.getIsActive() != null) {
                    existingBooksRelatedOptionDetails.setIsActive(booksRelatedOptionDetails.getIsActive());
                }

                return existingBooksRelatedOptionDetails;
            })
            .map(booksRelatedOptionDetailsRepository::save);
    }

    /**
     * Get all the booksRelatedOptionDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BooksRelatedOptionDetails> findAll(Pageable pageable) {
        log.debug("Request to get all BooksRelatedOptionDetails");
        return booksRelatedOptionDetailsRepository.findAll(pageable);
    }

    /**
     * Get one booksRelatedOptionDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BooksRelatedOptionDetails> findOne(Long id) {
        log.debug("Request to get BooksRelatedOptionDetails : {}", id);
        return booksRelatedOptionDetailsRepository.findById(id);
    }

    /**
     * Delete the booksRelatedOptionDetails by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BooksRelatedOptionDetails : {}", id);
        booksRelatedOptionDetailsRepository.deleteById(id);
    }
}
