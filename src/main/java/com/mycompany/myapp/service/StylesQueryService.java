package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Styles;
import com.mycompany.myapp.repository.StylesRepository;
import com.mycompany.myapp.service.criteria.StylesCriteria;
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
 * Service for executing complex queries for {@link Styles} entities in the database.
 * The main input is a {@link StylesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Styles} or a {@link Page} of {@link Styles} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StylesQueryService extends QueryService<Styles> {

    private final Logger log = LoggerFactory.getLogger(StylesQueryService.class);

    private final StylesRepository stylesRepository;

    public StylesQueryService(StylesRepository stylesRepository) {
        this.stylesRepository = stylesRepository;
    }

    /**
     * Return a {@link List} of {@link Styles} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Styles> findByCriteria(StylesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Styles> specification = createSpecification(criteria);
        return stylesRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Styles} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Styles> findByCriteria(StylesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Styles> specification = createSpecification(criteria);
        return stylesRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StylesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Styles> specification = createSpecification(criteria);
        return stylesRepository.count(specification);
    }

    /**
     * Function to convert {@link StylesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Styles> createSpecification(StylesCriteria criteria) {
        Specification<Styles> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Styles_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Styles_.code));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Styles_.description));
            }
            if (criteria.getImgURL() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImgURL(), Styles_.imgURL));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), Styles_.isActive));
            }
            if (criteria.getWidth() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWidth(), Styles_.width));
            }
            if (criteria.getHeight() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHeight(), Styles_.height));
            }
            if (criteria.getX() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getX(), Styles_.x));
            }
            if (criteria.getY() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getY(), Styles_.y));
            }
            if (criteria.getOptionsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getOptionsId(), root -> root.join(Styles_.options, JoinType.LEFT).get(Options_.id))
                    );
            }
        }
        return specification;
    }
}
