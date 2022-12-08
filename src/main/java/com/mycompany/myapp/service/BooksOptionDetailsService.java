package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.BooksOptionDetails;
import com.mycompany.myapp.repository.BooksOptionDetailsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BooksOptionDetails}.
 */
@Service
@Transactional
public class BooksOptionDetailsService {

    private final Logger log = LoggerFactory.getLogger(BooksOptionDetailsService.class);

    private final BooksOptionDetailsRepository booksOptionDetailsRepository;

    public BooksOptionDetailsService(BooksOptionDetailsRepository booksOptionDetailsRepository) {
        this.booksOptionDetailsRepository = booksOptionDetailsRepository;
    }

    /**
     * Save a booksOptionDetails.
     *
     * @param booksOptionDetails the entity to save.
     * @return the persisted entity.
     */
    public BooksOptionDetails save(BooksOptionDetails booksOptionDetails) {
        log.debug("Request to save BooksOptionDetails : {}", booksOptionDetails);
        return booksOptionDetailsRepository.save(booksOptionDetails);
    }

    /**
     * Update a booksOptionDetails.
     *
     * @param booksOptionDetails the entity to save.
     * @return the persisted entity.
     */
    public BooksOptionDetails update(BooksOptionDetails booksOptionDetails) {
        log.debug("Request to update BooksOptionDetails : {}", booksOptionDetails);
        return booksOptionDetailsRepository.save(booksOptionDetails);
    }

    /**
     * Partially update a booksOptionDetails.
     *
     * @param booksOptionDetails the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BooksOptionDetails> partialUpdate(BooksOptionDetails booksOptionDetails) {
        log.debug("Request to partially update BooksOptionDetails : {}", booksOptionDetails);

        return booksOptionDetailsRepository
            .findById(booksOptionDetails.getId())
            .map(existingBooksOptionDetails -> {
                if (booksOptionDetails.getAvatarAttributes() != null) {
                    existingBooksOptionDetails.setAvatarAttributes(booksOptionDetails.getAvatarAttributes());
                }
                if (booksOptionDetails.getAvatarCharactor() != null) {
                    existingBooksOptionDetails.setAvatarCharactor(booksOptionDetails.getAvatarCharactor());
                }
                if (booksOptionDetails.getStyle() != null) {
                    existingBooksOptionDetails.setStyle(booksOptionDetails.getStyle());
                }
                if (booksOptionDetails.getOption() != null) {
                    existingBooksOptionDetails.setOption(booksOptionDetails.getOption());
                }
                if (booksOptionDetails.getIsActive() != null) {
                    existingBooksOptionDetails.setIsActive(booksOptionDetails.getIsActive());
                }

                return existingBooksOptionDetails;
            })
            .map(booksOptionDetailsRepository::save);
    }

    /**
     * Get all the booksOptionDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BooksOptionDetails> findAll(Pageable pageable) {
        log.debug("Request to get all BooksOptionDetails");
        return booksOptionDetailsRepository.findAll(pageable);
    }

    /**
     * Get one booksOptionDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BooksOptionDetails> findOne(Long id) {
        log.debug("Request to get BooksOptionDetails : {}", id);
        return booksOptionDetailsRepository.findById(id);
    }

    /**
     * Delete the booksOptionDetails by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BooksOptionDetails : {}", id);
        booksOptionDetailsRepository.deleteById(id);
    }
}
