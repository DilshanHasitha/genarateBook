package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PriceRelatedOption;
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
public class PriceRelatedOptionRepositoryWithBagRelationshipsImpl implements PriceRelatedOptionRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<PriceRelatedOption> fetchBagRelationships(Optional<PriceRelatedOption> priceRelatedOption) {
        return priceRelatedOption.map(this::fetchPriceRelatedOptionDetails);
    }

    @Override
    public Page<PriceRelatedOption> fetchBagRelationships(Page<PriceRelatedOption> priceRelatedOptions) {
        return new PageImpl<>(
            fetchBagRelationships(priceRelatedOptions.getContent()),
            priceRelatedOptions.getPageable(),
            priceRelatedOptions.getTotalElements()
        );
    }

    @Override
    public List<PriceRelatedOption> fetchBagRelationships(List<PriceRelatedOption> priceRelatedOptions) {
        return Optional.of(priceRelatedOptions).map(this::fetchPriceRelatedOptionDetails).orElse(Collections.emptyList());
    }

    PriceRelatedOption fetchPriceRelatedOptionDetails(PriceRelatedOption result) {
        return entityManager
            .createQuery(
                "select priceRelatedOption from PriceRelatedOption priceRelatedOption left join fetch priceRelatedOption.priceRelatedOptionDetails where priceRelatedOption is :priceRelatedOption",
                PriceRelatedOption.class
            )
            .setParameter("priceRelatedOption", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<PriceRelatedOption> fetchPriceRelatedOptionDetails(List<PriceRelatedOption> priceRelatedOptions) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, priceRelatedOptions.size()).forEach(index -> order.put(priceRelatedOptions.get(index).getId(), index));
        List<PriceRelatedOption> result = entityManager
            .createQuery(
                "select distinct priceRelatedOption from PriceRelatedOption priceRelatedOption left join fetch priceRelatedOption.priceRelatedOptionDetails where priceRelatedOption in :priceRelatedOptions",
                PriceRelatedOption.class
            )
            .setParameter("priceRelatedOptions", priceRelatedOptions)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
