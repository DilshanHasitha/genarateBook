package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.SelectedOptionDetails;
import com.mycompany.myapp.repository.SelectedOptionDetailsRepository;
import com.mycompany.myapp.service.criteria.SelectedOptionDetailsCriteria;
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
 * Service for executing complex queries for {@link SelectedOptionDetails} entities in the database.
 * The main input is a {@link SelectedOptionDetailsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SelectedOptionDetails} or a {@link Page} of {@link SelectedOptionDetails} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SelectedOptionDetailsQueryService extends QueryService<SelectedOptionDetails> {

    private final Logger log = LoggerFactory.getLogger(SelectedOptionDetailsQueryService.class);

    private final SelectedOptionDetailsRepository selectedOptionDetailsRepository;

    public SelectedOptionDetailsQueryService(SelectedOptionDetailsRepository selectedOptionDetailsRepository) {
        this.selectedOptionDetailsRepository = selectedOptionDetailsRepository;
    }

    /**
     * Return a {@link List} of {@link SelectedOptionDetails} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SelectedOptionDetails> findByCriteria(SelectedOptionDetailsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SelectedOptionDetails> specification = createSpecification(criteria);
        return selectedOptionDetailsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link SelectedOptionDetails} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SelectedOptionDetails> findByCriteria(SelectedOptionDetailsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SelectedOptionDetails> specification = createSpecification(criteria);
        return selectedOptionDetailsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SelectedOptionDetailsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SelectedOptionDetails> specification = createSpecification(criteria);
        return selectedOptionDetailsRepository.count(specification);
    }

    /**
     * Function to convert {@link SelectedOptionDetailsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SelectedOptionDetails> createSpecification(SelectedOptionDetailsCriteria criteria) {
        Specification<SelectedOptionDetails> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SelectedOptionDetails_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), SelectedOptionDetails_.code));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), SelectedOptionDetails_.name));
            }
            if (criteria.getSelectedValue() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getSelectedValue(), SelectedOptionDetails_.selectedValue));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), SelectedOptionDetails_.isActive));
            }
            if (criteria.getSelectedOptionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSelectedOptionId(),
                            root -> root.join(SelectedOptionDetails_.selectedOptions, JoinType.LEFT).get(SelectedOption_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
