package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.PageLayers;
import com.mycompany.myapp.repository.PageLayersRepository;
import com.mycompany.myapp.service.criteria.PageLayersCriteria;
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
 * Service for executing complex queries for {@link PageLayers} entities in the database.
 * The main input is a {@link PageLayersCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PageLayers} or a {@link Page} of {@link PageLayers} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PageLayersQueryService extends QueryService<PageLayers> {

    private final Logger log = LoggerFactory.getLogger(PageLayersQueryService.class);

    private final PageLayersRepository pageLayersRepository;

    public PageLayersQueryService(PageLayersRepository pageLayersRepository) {
        this.pageLayersRepository = pageLayersRepository;
    }

    /**
     * Return a {@link List} of {@link PageLayers} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PageLayers> findByCriteria(PageLayersCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PageLayers> specification = createSpecification(criteria);
        return pageLayersRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link PageLayers} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PageLayers> findByCriteria(PageLayersCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PageLayers> specification = createSpecification(criteria);
        return pageLayersRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PageLayersCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PageLayers> specification = createSpecification(criteria);
        return pageLayersRepository.count(specification);
    }

    /**
     * Function to convert {@link PageLayersCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PageLayers> createSpecification(PageLayersCriteria criteria) {
        Specification<PageLayers> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PageLayers_.id));
            }
            if (criteria.getLayerNo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLayerNo(), PageLayers_.layerNo));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), PageLayers_.isActive));
            }
            if (criteria.getIsEditable() != null) {
                specification = specification.and(buildSpecification(criteria.getIsEditable(), PageLayers_.isEditable));
            }
            if (criteria.getIsText() != null) {
                specification = specification.and(buildSpecification(criteria.getIsText(), PageLayers_.isText));
            }
            if (criteria.getPageElementDetailsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPageElementDetailsId(),
                            root -> root.join(PageLayers_.pageElementDetails, JoinType.LEFT).get(PageLayersDetails_.id)
                        )
                    );
            }
            if (criteria.getBooksPageId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBooksPageId(),
                            root -> root.join(PageLayers_.booksPages, JoinType.LEFT).get(BooksPage_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
