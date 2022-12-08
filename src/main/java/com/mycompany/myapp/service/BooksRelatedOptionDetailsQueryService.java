package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.BooksRelatedOptionDetails;
import com.mycompany.myapp.repository.BooksRelatedOptionDetailsRepository;
import com.mycompany.myapp.service.criteria.BooksRelatedOptionDetailsCriteria;
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
 * Service for executing complex queries for {@link BooksRelatedOptionDetails} entities in the database.
 * The main input is a {@link BooksRelatedOptionDetailsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BooksRelatedOptionDetails} or a {@link Page} of {@link BooksRelatedOptionDetails} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BooksRelatedOptionDetailsQueryService extends QueryService<BooksRelatedOptionDetails> {

    private final Logger log = LoggerFactory.getLogger(BooksRelatedOptionDetailsQueryService.class);

    private final BooksRelatedOptionDetailsRepository booksRelatedOptionDetailsRepository;

    public BooksRelatedOptionDetailsQueryService(BooksRelatedOptionDetailsRepository booksRelatedOptionDetailsRepository) {
        this.booksRelatedOptionDetailsRepository = booksRelatedOptionDetailsRepository;
    }

    /**
     * Return a {@link List} of {@link BooksRelatedOptionDetails} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BooksRelatedOptionDetails> findByCriteria(BooksRelatedOptionDetailsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<BooksRelatedOptionDetails> specification = createSpecification(criteria);
        return booksRelatedOptionDetailsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link BooksRelatedOptionDetails} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BooksRelatedOptionDetails> findByCriteria(BooksRelatedOptionDetailsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BooksRelatedOptionDetails> specification = createSpecification(criteria);
        return booksRelatedOptionDetailsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BooksRelatedOptionDetailsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BooksRelatedOptionDetails> specification = createSpecification(criteria);
        return booksRelatedOptionDetailsRepository.count(specification);
    }

    /**
     * Function to convert {@link BooksRelatedOptionDetailsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BooksRelatedOptionDetails> createSpecification(BooksRelatedOptionDetailsCriteria criteria) {
        Specification<BooksRelatedOptionDetails> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), BooksRelatedOptionDetails_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), BooksRelatedOptionDetails_.code));
            }
            if (criteria.getDescription() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getDescription(), BooksRelatedOptionDetails_.description));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), BooksRelatedOptionDetails_.isActive));
            }
            if (criteria.getBooksRelatedOptionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBooksRelatedOptionId(),
                            root -> root.join(BooksRelatedOptionDetails_.booksRelatedOptions, JoinType.LEFT).get(BooksRelatedOption_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
