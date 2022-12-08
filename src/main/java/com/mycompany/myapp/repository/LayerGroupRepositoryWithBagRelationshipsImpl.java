package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.LayerGroup;
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
public class LayerGroupRepositoryWithBagRelationshipsImpl implements LayerGroupRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<LayerGroup> fetchBagRelationships(Optional<LayerGroup> layerGroup) {
        return layerGroup.map(this::fetchLayers);
    }

    @Override
    public Page<LayerGroup> fetchBagRelationships(Page<LayerGroup> layerGroups) {
        return new PageImpl<>(fetchBagRelationships(layerGroups.getContent()), layerGroups.getPageable(), layerGroups.getTotalElements());
    }

    @Override
    public List<LayerGroup> fetchBagRelationships(List<LayerGroup> layerGroups) {
        return Optional.of(layerGroups).map(this::fetchLayers).orElse(Collections.emptyList());
    }

    LayerGroup fetchLayers(LayerGroup result) {
        return entityManager
            .createQuery(
                "select layerGroup from LayerGroup layerGroup left join fetch layerGroup.layers where layerGroup is :layerGroup",
                LayerGroup.class
            )
            .setParameter("layerGroup", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<LayerGroup> fetchLayers(List<LayerGroup> layerGroups) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, layerGroups.size()).forEach(index -> order.put(layerGroups.get(index).getId(), index));
        List<LayerGroup> result = entityManager
            .createQuery(
                "select distinct layerGroup from LayerGroup layerGroup left join fetch layerGroup.layers where layerGroup in :layerGroups",
                LayerGroup.class
            )
            .setParameter("layerGroups", layerGroups)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
