package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.PriceRelatedOptionDetails;
import com.mycompany.myapp.repository.PriceRelatedOptionDetailsRepository;
import com.mycompany.myapp.service.criteria.PriceRelatedOptionDetailsCriteria;
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
 * Service for executing complex queries for {@link PriceRelatedOptionDetails} entities in the database.
 * The main input is a {@link PriceRelatedOptionDetailsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PriceRelatedOptionDetails} or a {@link Page} of {@link PriceRelatedOptionDetails} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PriceRelatedOptionDetailsQueryService extends QueryService<PriceRelatedOptionDetails> {

    private final Logger log = LoggerFactory.getLogger(PriceRelatedOptionDetailsQueryService.class);

    private final PriceRelatedOptionDetailsRepository priceRelatedOptionDetailsRepository;

    public PriceRelatedOptionDetailsQueryService(PriceRelatedOptionDetailsRepository priceRelatedOptionDetailsRepository) {
        this.priceRelatedOptionDetailsRepository = priceRelatedOptionDetailsRepository;
    }

    /**
     * Return a {@link List} of {@link PriceRelatedOptionDetails} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PriceRelatedOptionDetails> findByCriteria(PriceRelatedOptionDetailsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PriceRelatedOptionDetails> specification = createSpecification(criteria);
        return priceRelatedOptionDetailsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link PriceRelatedOptionDetails} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PriceRelatedOptionDetails> findByCriteria(PriceRelatedOptionDetailsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PriceRelatedOptionDetails> specification = createSpecification(criteria);
        return priceRelatedOptionDetailsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PriceRelatedOptionDetailsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PriceRelatedOptionDetails> specification = createSpecification(criteria);
        return priceRelatedOptionDetailsRepository.count(specification);
    }

    /**
     * Function to convert {@link PriceRelatedOptionDetailsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PriceRelatedOptionDetails> createSpecification(PriceRelatedOptionDetailsCriteria criteria) {
        Specification<PriceRelatedOptionDetails> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PriceRelatedOptionDetails_.id));
            }
            if (criteria.getDescription() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getDescription(), PriceRelatedOptionDetails_.description));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), PriceRelatedOptionDetails_.price));
            }
            if (criteria.getPriceRelatedOptionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPriceRelatedOptionId(),
                            root -> root.join(PriceRelatedOptionDetails_.priceRelatedOptions, JoinType.LEFT).get(PriceRelatedOption_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
