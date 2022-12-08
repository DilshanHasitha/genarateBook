package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.BooksRelatedOption;
import com.mycompany.myapp.repository.BooksRelatedOptionRepository;
import com.mycompany.myapp.service.criteria.BooksRelatedOptionCriteria;
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
 * Service for executing complex queries for {@link BooksRelatedOption} entities in the database.
 * The main input is a {@link BooksRelatedOptionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BooksRelatedOption} or a {@link Page} of {@link BooksRelatedOption} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BooksRelatedOptionQueryService extends QueryService<BooksRelatedOption> {

    private final Logger log = LoggerFactory.getLogger(BooksRelatedOptionQueryService.class);

    private final BooksRelatedOptionRepository booksRelatedOptionRepository;

    public BooksRelatedOptionQueryService(BooksRelatedOptionRepository booksRelatedOptionRepository) {
        this.booksRelatedOptionRepository = booksRelatedOptionRepository;
    }

    /**
     * Return a {@link List} of {@link BooksRelatedOption} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BooksRelatedOption> findByCriteria(BooksRelatedOptionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<BooksRelatedOption> specification = createSpecification(criteria);
        return booksRelatedOptionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link BooksRelatedOption} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BooksRelatedOption> findByCriteria(BooksRelatedOptionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BooksRelatedOption> specification = createSpecification(criteria);
        return booksRelatedOptionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BooksRelatedOptionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BooksRelatedOption> specification = createSpecification(criteria);
        return booksRelatedOptionRepository.count(specification);
    }

    /**
     * Function to convert {@link BooksRelatedOptionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BooksRelatedOption> createSpecification(BooksRelatedOptionCriteria criteria) {
        Specification<BooksRelatedOption> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), BooksRelatedOption_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), BooksRelatedOption_.code));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), BooksRelatedOption_.name));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), BooksRelatedOption_.isActive));
            }
            if (criteria.getBooksRelatedOptionDetailsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBooksRelatedOptionDetailsId(),
                            root ->
                                root.join(BooksRelatedOption_.booksRelatedOptionDetails, JoinType.LEFT).get(BooksRelatedOptionDetails_.id)
                        )
                    );
            }
            if (criteria.getBooksId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBooksId(),
                            root -> root.join(BooksRelatedOption_.books, JoinType.LEFT).get(Books_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
