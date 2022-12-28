package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.ImageStoreType;
import com.mycompany.myapp.repository.ImageStoreTypeRepository;
import com.mycompany.myapp.service.criteria.ImageStoreTypeCriteria;
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
 * Service for executing complex queries for {@link ImageStoreType} entities in the database.
 * The main input is a {@link ImageStoreTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ImageStoreType} or a {@link Page} of {@link ImageStoreType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ImageStoreTypeQueryService extends QueryService<ImageStoreType> {

    private final Logger log = LoggerFactory.getLogger(ImageStoreTypeQueryService.class);

    private final ImageStoreTypeRepository imageStoreTypeRepository;

    public ImageStoreTypeQueryService(ImageStoreTypeRepository imageStoreTypeRepository) {
        this.imageStoreTypeRepository = imageStoreTypeRepository;
    }

    /**
     * Return a {@link List} of {@link ImageStoreType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ImageStoreType> findByCriteria(ImageStoreTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ImageStoreType> specification = createSpecification(criteria);
        return imageStoreTypeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ImageStoreType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ImageStoreType> findByCriteria(ImageStoreTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ImageStoreType> specification = createSpecification(criteria);
        return imageStoreTypeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ImageStoreTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ImageStoreType> specification = createSpecification(criteria);
        return imageStoreTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link ImageStoreTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ImageStoreType> createSpecification(ImageStoreTypeCriteria criteria) {
        Specification<ImageStoreType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ImageStoreType_.id));
            }
            if (criteria.getImageStoreTypeCode() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getImageStoreTypeCode(), ImageStoreType_.imageStoreTypeCode));
            }
            if (criteria.getImageStoreTypeDescription() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getImageStoreTypeDescription(), ImageStoreType_.imageStoreTypeDescription)
                    );
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), ImageStoreType_.isActive));
            }
        }
        return specification;
    }
}
