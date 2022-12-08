package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.BooksPage;
import com.mycompany.myapp.repository.BooksPageRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BooksPage}.
 */
@Service
@Transactional
public class BooksPageService {

    private final Logger log = LoggerFactory.getLogger(BooksPageService.class);

    private final BooksPageRepository booksPageRepository;

    public BooksPageService(BooksPageRepository booksPageRepository) {
        this.booksPageRepository = booksPageRepository;
    }

    /**
     * Save a booksPage.
     *
     * @param booksPage the entity to save.
     * @return the persisted entity.
     */
    public BooksPage save(BooksPage booksPage) {
        log.debug("Request to save BooksPage : {}", booksPage);
        return booksPageRepository.save(booksPage);
    }

    /**
     * Update a booksPage.
     *
     * @param booksPage the entity to save.
     * @return the persisted entity.
     */
    public BooksPage update(BooksPage booksPage) {
        log.debug("Request to update BooksPage : {}", booksPage);
        return booksPageRepository.save(booksPage);
    }

    /**
     * Partially update a booksPage.
     *
     * @param booksPage the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BooksPage> partialUpdate(BooksPage booksPage) {
        log.debug("Request to partially update BooksPage : {}", booksPage);

        return booksPageRepository
            .findById(booksPage.getId())
            .map(existingBooksPage -> {
                if (booksPage.getNum() != null) {
                    existingBooksPage.setNum(booksPage.getNum());
                }
                if (booksPage.getIsActive() != null) {
                    existingBooksPage.setIsActive(booksPage.getIsActive());
                }

                return existingBooksPage;
            })
            .map(booksPageRepository::save);
    }

    /**
     * Get all the booksPages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BooksPage> findAll(Pageable pageable) {
        log.debug("Request to get all BooksPages");
        return booksPageRepository.findAll(pageable);
    }

    /**
     * Get all the booksPages with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<BooksPage> findAllWithEagerRelationships(Pageable pageable) {
        return booksPageRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one booksPage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BooksPage> findOne(Long id) {
        log.debug("Request to get BooksPage : {}", id);
        return booksPageRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the booksPage by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BooksPage : {}", id);
        booksPageRepository.deleteById(id);
    }
}
