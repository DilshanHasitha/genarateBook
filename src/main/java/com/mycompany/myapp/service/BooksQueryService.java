package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Books;
import com.mycompany.myapp.repository.BooksRepository;
import com.mycompany.myapp.service.criteria.BooksCriteria;
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
 * Service for executing complex queries for {@link Books} entities in the database.
 * The main input is a {@link BooksCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Books} or a {@link Page} of {@link Books} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BooksQueryService extends QueryService<Books> {

    private final Logger log = LoggerFactory.getLogger(BooksQueryService.class);

    private final BooksRepository booksRepository;

    public BooksQueryService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    /**
     * Return a {@link List} of {@link Books} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Books> findByCriteria(BooksCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Books> specification = createSpecification(criteria);
        return booksRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Books} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Books> findByCriteria(BooksCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Books> specification = createSpecification(criteria);
        return booksRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BooksCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Books> specification = createSpecification(criteria);
        return booksRepository.count(specification);
    }

    /**
     * Function to convert {@link BooksCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Books> createSpecification(BooksCriteria criteria) {
        Specification<Books> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Books_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Books_.code));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Books_.name));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Books_.title));
            }
            if (criteria.getSubTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSubTitle(), Books_.subTitle));
            }
            if (criteria.getAuthor() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAuthor(), Books_.author));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), Books_.isActive));
            }
            if (criteria.getNoOfPages() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNoOfPages(), Books_.noOfPages));
            }
            if (criteria.getStoreImg() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStoreImg(), Books_.storeImg));
            }
            if (criteria.getPageSizeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPageSizeId(), root -> root.join(Books_.pageSize, JoinType.LEFT).get(PageSize_.id))
                    );
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Books_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getBooksPageId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBooksPageId(),
                            root -> root.join(Books_.booksPages, JoinType.LEFT).get(BooksPage_.id)
                        )
                    );
            }
            if (criteria.getPriceRelatedOptionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPriceRelatedOptionId(),
                            root -> root.join(Books_.priceRelatedOptions, JoinType.LEFT).get(PriceRelatedOption_.id)
                        )
                    );
            }
            if (criteria.getBooksRelatedOptionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBooksRelatedOptionId(),
                            root -> root.join(Books_.booksRelatedOptions, JoinType.LEFT).get(BooksRelatedOption_.id)
                        )
                    );
            }
            if (criteria.getBooksAttributesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBooksAttributesId(),
                            root -> root.join(Books_.booksAttributes, JoinType.LEFT).get(BooksAttributes_.id)
                        )
                    );
            }
            if (criteria.getBooksVariablesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBooksVariablesId(),
                            root -> root.join(Books_.booksVariables, JoinType.LEFT).get(BooksVariables_.id)
                        )
                    );
            }
            if (criteria.getAvatarAttributesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAvatarAttributesId(),
                            root -> root.join(Books_.avatarAttributes, JoinType.LEFT).get(AvatarAttributes_.id)
                        )
                    );
            }
            if (criteria.getLayerGroupId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLayerGroupId(),
                            root -> root.join(Books_.layerGroups, JoinType.LEFT).get(LayerGroup_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
