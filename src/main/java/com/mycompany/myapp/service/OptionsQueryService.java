package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Options;
import com.mycompany.myapp.repository.OptionsRepository;
import com.mycompany.myapp.service.criteria.OptionsCriteria;
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
 * Service for executing complex queries for {@link Options} entities in the database.
 * The main input is a {@link OptionsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Options} or a {@link Page} of {@link Options} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OptionsQueryService extends QueryService<Options> {

    private final Logger log = LoggerFactory.getLogger(OptionsQueryService.class);

    private final OptionsRepository optionsRepository;

    public OptionsQueryService(OptionsRepository optionsRepository) {
        this.optionsRepository = optionsRepository;
    }

    /**
     * Return a {@link List} of {@link Options} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Options> findByCriteria(OptionsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Options> specification = createSpecification(criteria);
        return optionsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Options} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Options> findByCriteria(OptionsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Options> specification = createSpecification(criteria);
        return optionsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OptionsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Options> specification = createSpecification(criteria);
        return optionsRepository.count(specification);
    }

    /**
     * Function to convert {@link OptionsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Options> createSpecification(OptionsCriteria criteria) {
        Specification<Options> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Options_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Options_.code));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Options_.description));
            }
            if (criteria.getImgURL() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImgURL(), Options_.imgURL));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), Options_.isActive));
            }
            if (criteria.getStyleId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getStyleId(), root -> root.join(Options_.styles, JoinType.LEFT).get(Styles_.id))
                    );
            }
            if (criteria.getAvatarAttributesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAvatarAttributesId(),
                            root -> root.join(Options_.avatarAttributes, JoinType.LEFT).get(AvatarAttributes_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
