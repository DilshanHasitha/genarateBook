package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.LayerDetails;
import com.mycompany.myapp.repository.LayerDetailsRepository;
import com.mycompany.myapp.service.criteria.LayerDetailsCriteria;
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
 * Service for executing complex queries for {@link LayerDetails} entities in the database.
 * The main input is a {@link LayerDetailsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LayerDetails} or a {@link Page} of {@link LayerDetails} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LayerDetailsQueryService extends QueryService<LayerDetails> {

    private final Logger log = LoggerFactory.getLogger(LayerDetailsQueryService.class);

    private final LayerDetailsRepository layerDetailsRepository;

    public LayerDetailsQueryService(LayerDetailsRepository layerDetailsRepository) {
        this.layerDetailsRepository = layerDetailsRepository;
    }

    /**
     * Return a {@link List} of {@link LayerDetails} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LayerDetails> findByCriteria(LayerDetailsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LayerDetails> specification = createSpecification(criteria);
        return layerDetailsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link LayerDetails} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LayerDetails> findByCriteria(LayerDetailsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LayerDetails> specification = createSpecification(criteria);
        return layerDetailsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LayerDetailsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LayerDetails> specification = createSpecification(criteria);
        return layerDetailsRepository.count(specification);
    }

    /**
     * Function to convert {@link LayerDetailsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LayerDetails> createSpecification(LayerDetailsCriteria criteria) {
        Specification<LayerDetails> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LayerDetails_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), LayerDetails_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), LayerDetails_.description));
            }
            if (criteria.getLayersId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getLayersId(), root -> root.join(LayerDetails_.layers, JoinType.LEFT).get(Layers_.id))
                    );
            }
        }
        return specification;
    }
}
