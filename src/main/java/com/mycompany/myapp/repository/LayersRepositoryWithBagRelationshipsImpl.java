package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Layers;
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
public class LayersRepositoryWithBagRelationshipsImpl implements LayersRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Layers> fetchBagRelationships(Optional<Layers> layers) {
        return layers.map(this::fetchLayerdetails);
    }

    @Override
    public Page<Layers> fetchBagRelationships(Page<Layers> layers) {
        return new PageImpl<>(fetchBagRelationships(layers.getContent()), layers.getPageable(), layers.getTotalElements());
    }

    @Override
    public List<Layers> fetchBagRelationships(List<Layers> layers) {
        return Optional.of(layers).map(this::fetchLayerdetails).orElse(Collections.emptyList());
    }

    Layers fetchLayerdetails(Layers result) {
        return entityManager
            .createQuery("select layers from Layers layers left join fetch layers.layerdetails where layers is :layers", Layers.class)
            .setParameter("layers", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Layers> fetchLayerdetails(List<Layers> layers) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, layers.size()).forEach(index -> order.put(layers.get(index).getId(), index));
        List<Layers> result = entityManager
            .createQuery(
                "select distinct layers from Layers layers left join fetch layers.layerdetails where layers in :layers",
                Layers.class
            )
            .setParameter("layers", layers)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
