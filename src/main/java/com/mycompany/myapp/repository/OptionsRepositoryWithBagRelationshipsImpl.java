package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Options;
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
public class OptionsRepositoryWithBagRelationshipsImpl implements OptionsRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Options> fetchBagRelationships(Optional<Options> options) {
        return options.map(this::fetchStyles);
    }

    @Override
    public Page<Options> fetchBagRelationships(Page<Options> options) {
        return new PageImpl<>(fetchBagRelationships(options.getContent()), options.getPageable(), options.getTotalElements());
    }

    @Override
    public List<Options> fetchBagRelationships(List<Options> options) {
        return Optional.of(options).map(this::fetchStyles).orElse(Collections.emptyList());
    }

    Options fetchStyles(Options result) {
        return entityManager
            .createQuery("select options from Options options left join fetch options.styles where options is :options", Options.class)
            .setParameter("options", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Options> fetchStyles(List<Options> options) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, options.size()).forEach(index -> order.put(options.get(index).getId(), index));
        List<Options> result = entityManager
            .createQuery(
                "select distinct options from Options options left join fetch options.styles where options in :options",
                Options.class
            )
            .setParameter("options", options)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
