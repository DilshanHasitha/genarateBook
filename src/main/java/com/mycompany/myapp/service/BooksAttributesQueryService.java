package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.BooksAttributes;
import com.mycompany.myapp.repository.BooksAttributesRepository;
import com.mycompany.myapp.service.criteria.BooksAttributesCriteria;
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
 * Service for executing complex queries for {@link BooksAttributes} entities in the database.
 * The main input is a {@link BooksAttributesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BooksAttributes} or a {@link Page} of {@link BooksAttributes} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BooksAttributesQueryService extends QueryService<BooksAttributes> {

    private final Logger log = LoggerFactory.getLogger(BooksAttributesQueryService.class);

    private final BooksAttributesRepository booksAttributesRepository;

    public BooksAttributesQueryService(BooksAttributesRepository booksAttributesRepository) {
        this.booksAttributesRepository = booksAttributesRepository;
    }

    /**
     * Return a {@link List} of {@link BooksAttributes} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BooksAttributes> findByCriteria(BooksAttributesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<BooksAttributes> specification = createSpecification(criteria);
        return booksAttributesRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link BooksAttributes} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BooksAttributes> findByCriteria(BooksAttributesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BooksAttributes> specification = createSpecification(criteria);
        return booksAttributesRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BooksAttributesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BooksAttributes> specification = createSpecification(criteria);
        return booksAttributesRepository.count(specification);
    }

    /**
     * Function to convert {@link BooksAttributesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BooksAttributes> createSpecification(BooksAttributesCriteria criteria) {
        Specification<BooksAttributes> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), BooksAttributes_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), BooksAttributes_.code));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), BooksAttributes_.description));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), BooksAttributes_.isActive));
            }
            if (criteria.getBooksId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getBooksId(), root -> root.join(BooksAttributes_.books, JoinType.LEFT).get(Books_.id))
                    );
            }
        }
        return specification;
    }
}
