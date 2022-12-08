package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.PageLayersDetails;
import com.mycompany.myapp.repository.PageLayersDetailsRepository;
import com.mycompany.myapp.service.criteria.PageLayersDetailsCriteria;
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
 * Service for executing complex queries for {@link PageLayersDetails} entities in the database.
 * The main input is a {@link PageLayersDetailsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PageLayersDetails} or a {@link Page} of {@link PageLayersDetails} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PageLayersDetailsQueryService extends QueryService<PageLayersDetails> {

    private final Logger log = LoggerFactory.getLogger(PageLayersDetailsQueryService.class);

    private final PageLayersDetailsRepository pageLayersDetailsRepository;

    public PageLayersDetailsQueryService(PageLayersDetailsRepository pageLayersDetailsRepository) {
        this.pageLayersDetailsRepository = pageLayersDetailsRepository;
    }

    /**
     * Return a {@link List} of {@link PageLayersDetails} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PageLayersDetails> findByCriteria(PageLayersDetailsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PageLayersDetails> specification = createSpecification(criteria);
        return pageLayersDetailsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link PageLayersDetails} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PageLayersDetails> findByCriteria(PageLayersDetailsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PageLayersDetails> specification = createSpecification(criteria);
        return pageLayersDetailsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PageLayersDetailsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PageLayersDetails> specification = createSpecification(criteria);
        return pageLayersDetailsRepository.count(specification);
    }

    /**
     * Function to convert {@link PageLayersDetailsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PageLayersDetails> createSpecification(PageLayersDetailsCriteria criteria) {
        Specification<PageLayersDetails> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PageLayersDetails_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), PageLayersDetails_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), PageLayersDetails_.description));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), PageLayersDetails_.isActive));
            }
            if (criteria.getPageElementId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPageElementId(),
                            root -> root.join(PageLayersDetails_.pageElements, JoinType.LEFT).get(PageLayers_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
