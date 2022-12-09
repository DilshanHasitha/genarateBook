package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Books;
import com.mycompany.myapp.repository.BooksRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Books}.
 */
@Service
@Transactional
public class BooksService {

    private final Logger log = LoggerFactory.getLogger(BooksService.class);

    private final BooksRepository booksRepository;

    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    /**
     * Save a books.
     *
     * @param books the entity to save.
     * @return the persisted entity.
     */
    public Books save(Books books) {
        log.debug("Request to save Books : {}", books);
        return booksRepository.save(books);
    }

    /**
     * Update a books.
     *
     * @param books the entity to save.
     * @return the persisted entity.
     */
    public Books update(Books books) {
        log.debug("Request to update Books : {}", books);
        return booksRepository.save(books);
    }

    /**
     * Partially update a books.
     *
     * @param books the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Books> partialUpdate(Books books) {
        log.debug("Request to partially update Books : {}", books);

        return booksRepository
            .findById(books.getId())
            .map(existingBooks -> {
                if (books.getCode() != null) {
                    existingBooks.setCode(books.getCode());
                }
                if (books.getName() != null) {
                    existingBooks.setName(books.getName());
                }
                if (books.getTitle() != null) {
                    existingBooks.setTitle(books.getTitle());
                }
                if (books.getSubTitle() != null) {
                    existingBooks.setSubTitle(books.getSubTitle());
                }
                if (books.getAuthor() != null) {
                    existingBooks.setAuthor(books.getAuthor());
                }
                if (books.getIsActive() != null) {
                    existingBooks.setIsActive(books.getIsActive());
                }
                if (books.getNoOfPages() != null) {
                    existingBooks.setNoOfPages(books.getNoOfPages());
                }
                if (books.getStoreImg() != null) {
                    existingBooks.setStoreImg(books.getStoreImg());
                }

                return existingBooks;
            })
            .map(booksRepository::save);
    }

    /**
     * Get all the books.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Books> findAll(Pageable pageable) {
        log.debug("Request to get all Books");
        return booksRepository.findAll(pageable);
    }

    /**
     * Get all the books with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Books> findAllWithEagerRelationships(Pageable pageable) {
        return booksRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one books by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Books> findOne(Long id) {
        log.debug("Request to get Books : {}", id);
        return booksRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the books by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Books : {}", id);
        booksRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Books> findOneByCode(String code) {
        log.debug("Request to get Books : {}", code);
        return booksRepository.findOneByCode(code);
    }
}
