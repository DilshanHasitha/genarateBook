package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.BooksVariables;
import com.mycompany.myapp.repository.BooksVariablesRepository;
import com.mycompany.myapp.service.criteria.BooksVariablesCriteria;
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
 * Service for executing complex queries for {@link BooksVariables} entities in the database.
 * The main input is a {@link BooksVariablesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BooksVariables} or a {@link Page} of {@link BooksVariables} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BooksVariablesQueryService extends QueryService<BooksVariables> {

    private final Logger log = LoggerFactory.getLogger(BooksVariablesQueryService.class);

    private final BooksVariablesRepository booksVariablesRepository;

    public BooksVariablesQueryService(BooksVariablesRepository booksVariablesRepository) {
        this.booksVariablesRepository = booksVariablesRepository;
    }

    /**
     * Return a {@link List} of {@link BooksVariables} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BooksVariables> findByCriteria(BooksVariablesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<BooksVariables> specification = createSpecification(criteria);
        return booksVariablesRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link BooksVariables} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BooksVariables> findByCriteria(BooksVariablesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BooksVariables> specification = createSpecification(criteria);
        return booksVariablesRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BooksVariablesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BooksVariables> specification = createSpecification(criteria);
        return booksVariablesRepository.count(specification);
    }

    /**
     * Function to convert {@link BooksVariablesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BooksVariables> createSpecification(BooksVariablesCriteria criteria) {
        Specification<BooksVariables> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), BooksVariables_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), BooksVariables_.code));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), BooksVariables_.description));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), BooksVariables_.isActive));
            }
            if (criteria.getBooksId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getBooksId(), root -> root.join(BooksVariables_.books, JoinType.LEFT).get(Books_.id))
                    );
            }
        }
        return specification;
    }
}
