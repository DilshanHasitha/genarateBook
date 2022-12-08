package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.BooksOptionDetails;
import com.mycompany.myapp.repository.BooksOptionDetailsRepository;
import com.mycompany.myapp.service.criteria.BooksOptionDetailsCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link BooksOptionDetails} entities in the database.
 * The main input is a {@link BooksOptionDetailsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BooksOptionDetails} or a {@link Page} of {@link BooksOptionDetails} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BooksOptionDetailsQueryService extends QueryService<BooksOptionDetails> {

    private final Logger log = LoggerFactory.getLogger(BooksOptionDetailsQueryService.class);

    private final BooksOptionDetailsRepository booksOptionDetailsRepository;

    public BooksOptionDetailsQueryService(BooksOptionDetailsRepository booksOptionDetailsRepository) {
        this.booksOptionDetailsRepository = booksOptionDetailsRepository;
    }

    /**
     * Return a {@link List} of {@link BooksOptionDetails} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BooksOptionDetails> findByCriteria(BooksOptionDetailsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<BooksOptionDetails> specification = createSpecification(criteria);
        return booksOptionDetailsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link BooksOptionDetails} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BooksOptionDetails> findByCriteria(BooksOptionDetailsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BooksOptionDetails> specification = createSpecification(criteria);
        return booksOptionDetailsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BooksOptionDetailsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BooksOptionDetails> specification = createSpecification(criteria);
        return booksOptionDetailsRepository.count(specification);
    }

    /**
     * Function to convert {@link BooksOptionDetailsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BooksOptionDetails> createSpecification(BooksOptionDetailsCriteria criteria) {
        Specification<BooksOptionDetails> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), BooksOptionDetails_.id));
            }
            if (criteria.getAvatarAttributes() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getAvatarAttributes(), BooksOptionDetails_.avatarAttributes));
            }
            if (criteria.getAvatarCharactor() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getAvatarCharactor(), BooksOptionDetails_.avatarCharactor));
            }
            if (criteria.getStyle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStyle(), BooksOptionDetails_.style));
            }
            if (criteria.getOption() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOption(), BooksOptionDetails_.option));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), BooksOptionDetails_.isActive));
            }
            if (criteria.getBooksId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBooksId(),
                            root -> root.join(BooksOptionDetails_.books, JoinType.LEFT).get(Books_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
