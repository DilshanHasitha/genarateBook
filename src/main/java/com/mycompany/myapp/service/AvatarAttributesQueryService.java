package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.AvatarAttributes;
import com.mycompany.myapp.repository.AvatarAttributesRepository;
import com.mycompany.myapp.service.criteria.AvatarAttributesCriteria;
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
 * Service for executing complex queries for {@link AvatarAttributes} entities in the database.
 * The main input is a {@link AvatarAttributesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AvatarAttributes} or a {@link Page} of {@link AvatarAttributes} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AvatarAttributesQueryService extends QueryService<AvatarAttributes> {

    private final Logger log = LoggerFactory.getLogger(AvatarAttributesQueryService.class);

    private final AvatarAttributesRepository avatarAttributesRepository;

    public AvatarAttributesQueryService(AvatarAttributesRepository avatarAttributesRepository) {
        this.avatarAttributesRepository = avatarAttributesRepository;
    }

    /**
     * Return a {@link List} of {@link AvatarAttributes} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AvatarAttributes> findByCriteria(AvatarAttributesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AvatarAttributes> specification = createSpecification(criteria);
        return avatarAttributesRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link AvatarAttributes} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AvatarAttributes> findByCriteria(AvatarAttributesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AvatarAttributes> specification = createSpecification(criteria);
        return avatarAttributesRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AvatarAttributesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AvatarAttributes> specification = createSpecification(criteria);
        return avatarAttributesRepository.count(specification);
    }

    /**
     * Function to convert {@link AvatarAttributesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AvatarAttributes> createSpecification(AvatarAttributesCriteria criteria) {
        Specification<AvatarAttributes> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AvatarAttributes_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), AvatarAttributes_.code));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), AvatarAttributes_.description));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), AvatarAttributes_.isActive));
            }
            if (criteria.getAvatarCharactorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAvatarCharactorId(),
                            root -> root.join(AvatarAttributes_.avatarCharactors, JoinType.LEFT).get(AvatarCharactor_.id)
                        )
                    );
            }
            if (criteria.getOptionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOptionId(),
                            root -> root.join(AvatarAttributes_.options, JoinType.LEFT).get(Options_.id)
                        )
                    );
            }
            if (criteria.getBooksId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getBooksId(), root -> root.join(AvatarAttributes_.books, JoinType.LEFT).get(Books_.id))
                    );
            }
        }
        return specification;
    }
}
