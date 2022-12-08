package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.BooksPage;
import com.mycompany.myapp.repository.BooksPageRepository;
import com.mycompany.myapp.service.criteria.BooksPageCriteria;
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
 * Service for executing complex queries for {@link BooksPage} entities in the database.
 * The main input is a {@link BooksPageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BooksPage} or a {@link Page} of {@link BooksPage} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BooksPageQueryService extends QueryService<BooksPage> {

    private final Logger log = LoggerFactory.getLogger(BooksPageQueryService.class);

    private final BooksPageRepository booksPageRepository;

    public BooksPageQueryService(BooksPageRepository booksPageRepository) {
        this.booksPageRepository = booksPageRepository;
    }

    /**
     * Return a {@link List} of {@link BooksPage} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BooksPage> findByCriteria(BooksPageCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<BooksPage> specification = createSpecification(criteria);
        return booksPageRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link BooksPage} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BooksPage> findByCriteria(BooksPageCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BooksPage> specification = createSpecification(criteria);
        return booksPageRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BooksPageCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BooksPage> specification = createSpecification(criteria);
        return booksPageRepository.count(specification);
    }

    /**
     * Function to convert {@link BooksPageCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BooksPage> createSpecification(BooksPageCriteria criteria) {
        Specification<BooksPage> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), BooksPage_.id));
            }
            if (criteria.getNum() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNum(), BooksPage_.num));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), BooksPage_.isActive));
            }
            if (criteria.getPageDetailsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPageDetailsId(),
                            root -> root.join(BooksPage_.pageDetails, JoinType.LEFT).get(PageLayers_.id)
                        )
                    );
            }
            if (criteria.getBooksId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getBooksId(), root -> root.join(BooksPage_.books, JoinType.LEFT).get(Books_.id))
                    );
            }
        }
        return specification;
    }
}
