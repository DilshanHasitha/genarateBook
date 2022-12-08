package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.PageSize;
import com.mycompany.myapp.repository.PageSizeRepository;
import com.mycompany.myapp.service.criteria.PageSizeCriteria;
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
 * Service for executing complex queries for {@link PageSize} entities in the database.
 * The main input is a {@link PageSizeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PageSize} or a {@link Page} of {@link PageSize} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PageSizeQueryService extends QueryService<PageSize> {

    private final Logger log = LoggerFactory.getLogger(PageSizeQueryService.class);

    private final PageSizeRepository pageSizeRepository;

    public PageSizeQueryService(PageSizeRepository pageSizeRepository) {
        this.pageSizeRepository = pageSizeRepository;
    }

    /**
     * Return a {@link List} of {@link PageSize} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PageSize> findByCriteria(PageSizeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PageSize> specification = createSpecification(criteria);
        return pageSizeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link PageSize} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PageSize> findByCriteria(PageSizeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PageSize> specification = createSpecification(criteria);
        return pageSizeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PageSizeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PageSize> specification = createSpecification(criteria);
        return pageSizeRepository.count(specification);
    }

    /**
     * Function to convert {@link PageSizeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PageSize> createSpecification(PageSizeCriteria criteria) {
        Specification<PageSize> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PageSize_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), PageSize_.code));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), PageSize_.description));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), PageSize_.isActive));
            }
            if (criteria.getWidth() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWidth(), PageSize_.width));
            }
            if (criteria.getHeight() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHeight(), PageSize_.height));
            }
        }
        return specification;
    }
}
