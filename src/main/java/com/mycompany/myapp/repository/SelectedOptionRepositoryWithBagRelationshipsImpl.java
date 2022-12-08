package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SelectedOption;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class SelectedOptionRepositoryWithBagRelationshipsImpl implements SelectedOptionRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<SelectedOption> fetchBagRelationships(Optional<SelectedOption> selectedOption) {
        return selectedOption.map(this::fetchSelectedOptionDetails);
    }

    @Override
    public Page<SelectedOption> fetchBagRelationships(Page<SelectedOption> selectedOptions) {
        return new PageImpl<>(
            fetchBagRelationships(selectedOptions.getContent()),
            selectedOptions.getPageable(),
            selectedOptions.getTotalElements()
        );
    }

    @Override
    public List<SelectedOption> fetchBagRelationships(List<SelectedOption> selectedOptions) {
        return Optional.of(selectedOptions).map(this::fetchSelectedOptionDetails).orElse(Collections.emptyList());
    }

    SelectedOption fetchSelectedOptionDetails(SelectedOption result) {
        return entityManager
            .createQuery(
                "select selectedOption from SelectedOption selectedOption left join fetch selectedOption.selectedOptionDetails where selectedOption is :selectedOption",
                SelectedOption.class
            )
            .setParameter("selectedOption", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<SelectedOption> fetchSelectedOptionDetails(List<SelectedOption> selectedOptions) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, selectedOptions.size()).forEach(index -> order.put(selectedOptions.get(index).getId(), index));
        List<SelectedOption> result = entityManager
            .createQuery(
                "select distinct selectedOption from SelectedOption selectedOption left join fetch selectedOption.selectedOptionDetails where selectedOption in :selectedOptions",
                SelectedOption.class
            )
            .setParameter("selectedOptions", selectedOptions)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
