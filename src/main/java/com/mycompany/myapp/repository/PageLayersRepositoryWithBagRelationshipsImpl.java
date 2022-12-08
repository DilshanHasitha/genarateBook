package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PageLayers;
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
public class PageLayersRepositoryWithBagRelationshipsImpl implements PageLayersRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<PageLayers> fetchBagRelationships(Optional<PageLayers> pageLayers) {
        return pageLayers.map(this::fetchPageElementDetails);
    }

    @Override
    public Page<PageLayers> fetchBagRelationships(Page<PageLayers> pageLayers) {
        return new PageImpl<>(fetchBagRelationships(pageLayers.getContent()), pageLayers.getPageable(), pageLayers.getTotalElements());
    }

    @Override
    public List<PageLayers> fetchBagRelationships(List<PageLayers> pageLayers) {
        return Optional.of(pageLayers).map(this::fetchPageElementDetails).orElse(Collections.emptyList());
    }

    PageLayers fetchPageElementDetails(PageLayers result) {
        return entityManager
            .createQuery(
                "select pageLayers from PageLayers pageLayers left join fetch pageLayers.pageElementDetails where pageLayers is :pageLayers",
                PageLayers.class
            )
            .setParameter("pageLayers", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<PageLayers> fetchPageElementDetails(List<PageLayers> pageLayers) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, pageLayers.size()).forEach(index -> order.put(pageLayers.get(index).getId(), index));
        List<PageLayers> result = entityManager
            .createQuery(
                "select distinct pageLayers from PageLayers pageLayers left join fetch pageLayers.pageElementDetails where pageLayers in :pageLayers",
                PageLayers.class
            )
            .setParameter("pageLayers", pageLayers)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
