package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Styles;
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
public class StylesRepositoryWithBagRelationshipsImpl implements StylesRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Styles> fetchBagRelationships(Optional<Styles> styles) {
        return styles.map(this::fetchOptions);
    }

    @Override
    public Page<Styles> fetchBagRelationships(Page<Styles> styles) {
        return new PageImpl<>(fetchBagRelationships(styles.getContent()), styles.getPageable(), styles.getTotalElements());
    }

    @Override
    public List<Styles> fetchBagRelationships(List<Styles> styles) {
        return Optional.of(styles).map(this::fetchOptions).orElse(Collections.emptyList());
    }

    Styles fetchOptions(Styles result) {
        return entityManager
            .createQuery("select styles from Styles styles left join fetch styles.options where styles is :styles", Styles.class)
            .setParameter("styles", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Styles> fetchOptions(List<Styles> styles) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, styles.size()).forEach(index -> order.put(styles.get(index).getId(), index));
        List<Styles> result = entityManager
            .createQuery("select distinct styles from Styles styles left join fetch styles.options where styles in :styles", Styles.class)
            .setParameter("styles", styles)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
