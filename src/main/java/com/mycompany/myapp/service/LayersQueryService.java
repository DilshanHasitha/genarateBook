package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Layers;
import com.mycompany.myapp.repository.LayersRepository;
import com.mycompany.myapp.service.criteria.LayersCriteria;
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
 * Service for executing complex queries for {@link Layers} entities in the database.
 * The main input is a {@link LayersCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Layers} or a {@link Page} of {@link Layers} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LayersQueryService extends QueryService<Layers> {

    private final Logger log = LoggerFactory.getLogger(LayersQueryService.class);

    private final LayersRepository layersRepository;

    public LayersQueryService(LayersRepository layersRepository) {
        this.layersRepository = layersRepository;
    }

    /**
     * Return a {@link List} of {@link Layers} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Layers> findByCriteria(LayersCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Layers> specification = createSpecification(criteria);
        return layersRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Layers} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Layers> findByCriteria(LayersCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Layers> specification = createSpecification(criteria);
        return layersRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LayersCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Layers> specification = createSpecification(criteria);
        return layersRepository.count(specification);
    }

    /**
     * Function to convert {@link LayersCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Layers> createSpecification(LayersCriteria criteria) {
        Specification<Layers> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Layers_.id));
            }
            if (criteria.getLayerNo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLayerNo(), Layers_.layerNo));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), Layers_.isActive));
            }
            if (criteria.getLayerdetailsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLayerdetailsId(),
                            root -> root.join(Layers_.layerdetails, JoinType.LEFT).get(LayerDetails_.id)
                        )
                    );
            }
            if (criteria.getLayerGroupId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLayerGroupId(),
                            root -> root.join(Layers_.layerGroups, JoinType.LEFT).get(LayerGroup_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
