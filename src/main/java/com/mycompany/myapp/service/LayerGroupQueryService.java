package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.LayerGroup;
import com.mycompany.myapp.repository.LayerGroupRepository;
import com.mycompany.myapp.service.criteria.LayerGroupCriteria;
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
 * Service for executing complex queries for {@link LayerGroup} entities in the database.
 * The main input is a {@link LayerGroupCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LayerGroup} or a {@link Page} of {@link LayerGroup} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LayerGroupQueryService extends QueryService<LayerGroup> {

    private final Logger log = LoggerFactory.getLogger(LayerGroupQueryService.class);

    private final LayerGroupRepository layerGroupRepository;

    public LayerGroupQueryService(LayerGroupRepository layerGroupRepository) {
        this.layerGroupRepository = layerGroupRepository;
    }

    /**
     * Return a {@link List} of {@link LayerGroup} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LayerGroup> findByCriteria(LayerGroupCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LayerGroup> specification = createSpecification(criteria);
        return layerGroupRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link LayerGroup} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LayerGroup> findByCriteria(LayerGroupCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LayerGroup> specification = createSpecification(criteria);
        return layerGroupRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LayerGroupCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LayerGroup> specification = createSpecification(criteria);
        return layerGroupRepository.count(specification);
    }

    /**
     * Function to convert {@link LayerGroupCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LayerGroup> createSpecification(LayerGroupCriteria criteria) {
        Specification<LayerGroup> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LayerGroup_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), LayerGroup_.code));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), LayerGroup_.description));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), LayerGroup_.isActive));
            }
            if (criteria.getLayersId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getLayersId(), root -> root.join(LayerGroup_.layers, JoinType.LEFT).get(Layers_.id))
                    );
            }
            if (criteria.getBooksId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getBooksId(), root -> root.join(LayerGroup_.books, JoinType.LEFT).get(Books_.id))
                    );
            }
        }
        return specification;
    }
}
