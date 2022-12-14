package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.AvatarCharactor;
import com.mycompany.myapp.repository.AvatarCharactorRepository;
import com.mycompany.myapp.service.criteria.AvatarCharactorCriteria;
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
 * Service for executing complex queries for {@link AvatarCharactor} entities in the database.
 * The main input is a {@link AvatarCharactorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AvatarCharactor} or a {@link Page} of {@link AvatarCharactor} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AvatarCharactorQueryService extends QueryService<AvatarCharactor> {

    private final Logger log = LoggerFactory.getLogger(AvatarCharactorQueryService.class);

    private final AvatarCharactorRepository avatarCharactorRepository;

    public AvatarCharactorQueryService(AvatarCharactorRepository avatarCharactorRepository) {
        this.avatarCharactorRepository = avatarCharactorRepository;
    }

    /**
     * Return a {@link List} of {@link AvatarCharactor} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AvatarCharactor> findByCriteria(AvatarCharactorCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AvatarCharactor> specification = createSpecification(criteria);
        return avatarCharactorRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link AvatarCharactor} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AvatarCharactor> findByCriteria(AvatarCharactorCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AvatarCharactor> specification = createSpecification(criteria);
        return avatarCharactorRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AvatarCharactorCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AvatarCharactor> specification = createSpecification(criteria);
        return avatarCharactorRepository.count(specification);
    }

    /**
     * Function to convert {@link AvatarCharactorCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AvatarCharactor> createSpecification(AvatarCharactorCriteria criteria) {
        Specification<AvatarCharactor> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AvatarCharactor_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), AvatarCharactor_.code));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), AvatarCharactor_.description));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), AvatarCharactor_.isActive));
            }
            if (criteria.getImgUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImgUrl(), AvatarCharactor_.imgUrl));
            }
            if (criteria.getWidth() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWidth(), AvatarCharactor_.width));
            }
            if (criteria.getHeight() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHeight(), AvatarCharactor_.height));
            }
            if (criteria.getX() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getX(), AvatarCharactor_.x));
            }
            if (criteria.getY() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getY(), AvatarCharactor_.y));
            }
            if (criteria.getAvatarAttributesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAvatarAttributesId(),
                            root -> root.join(AvatarCharactor_.avatarAttributes, JoinType.LEFT).get(AvatarAttributes_.id)
                        )
                    );
            }
            if (criteria.getLayerGroupId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLayerGroupId(),
                            root -> root.join(AvatarCharactor_.layerGroup, JoinType.LEFT).get(LayerGroup_.id)
                        )
                    );
            }
            if (criteria.getCharacterId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCharacterId(),
                            root -> root.join(AvatarCharactor_.character, JoinType.LEFT).get(Character_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
