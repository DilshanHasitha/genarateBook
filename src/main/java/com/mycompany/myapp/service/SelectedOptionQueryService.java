package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.SelectedOption;
import com.mycompany.myapp.repository.SelectedOptionRepository;
import com.mycompany.myapp.service.criteria.SelectedOptionCriteria;
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
 * Service for executing complex queries for {@link SelectedOption} entities in the database.
 * The main input is a {@link SelectedOptionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SelectedOption} or a {@link Page} of {@link SelectedOption} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SelectedOptionQueryService extends QueryService<SelectedOption> {

    private final Logger log = LoggerFactory.getLogger(SelectedOptionQueryService.class);

    private final SelectedOptionRepository selectedOptionRepository;

    public SelectedOptionQueryService(SelectedOptionRepository selectedOptionRepository) {
        this.selectedOptionRepository = selectedOptionRepository;
    }

    /**
     * Return a {@link List} of {@link SelectedOption} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SelectedOption> findByCriteria(SelectedOptionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SelectedOption> specification = createSpecification(criteria);
        return selectedOptionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link SelectedOption} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SelectedOption> findByCriteria(SelectedOptionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SelectedOption> specification = createSpecification(criteria);
        return selectedOptionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SelectedOptionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SelectedOption> specification = createSpecification(criteria);
        return selectedOptionRepository.count(specification);
    }

    /**
     * Function to convert {@link SelectedOptionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SelectedOption> createSpecification(SelectedOptionCriteria criteria) {
        Specification<SelectedOption> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SelectedOption_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), SelectedOption_.code));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), SelectedOption_.date));
            }
            if (criteria.getBooksId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getBooksId(), root -> root.join(SelectedOption_.books, JoinType.LEFT).get(Books_.id))
                    );
            }
            if (criteria.getCustomerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCustomerId(),
                            root -> root.join(SelectedOption_.customer, JoinType.LEFT).get(Customer_.id)
                        )
                    );
            }
            if (criteria.getSelectedOptionDetailsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSelectedOptionDetailsId(),
                            root -> root.join(SelectedOption_.selectedOptionDetails, JoinType.LEFT).get(SelectedOptionDetails_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
