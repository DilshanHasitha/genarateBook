package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.AvatarAttributes;
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
public class AvatarAttributesRepositoryWithBagRelationshipsImpl implements AvatarAttributesRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<AvatarAttributes> fetchBagRelationships(Optional<AvatarAttributes> avatarAttributes) {
        return avatarAttributes.map(this::fetchAvatarCharactors).map(this::fetchStyles);
    }

    @Override
    public Page<AvatarAttributes> fetchBagRelationships(Page<AvatarAttributes> avatarAttributes) {
        return new PageImpl<>(
            fetchBagRelationships(avatarAttributes.getContent()),
            avatarAttributes.getPageable(),
            avatarAttributes.getTotalElements()
        );
    }

    @Override
    public List<AvatarAttributes> fetchBagRelationships(List<AvatarAttributes> avatarAttributes) {
        return Optional.of(avatarAttributes).map(this::fetchAvatarCharactors).map(this::fetchStyles).orElse(Collections.emptyList());
    }

    AvatarAttributes fetchAvatarCharactors(AvatarAttributes result) {
        return entityManager
            .createQuery(
                "select avatarAttributes from AvatarAttributes avatarAttributes left join fetch avatarAttributes.avatarCharactors where avatarAttributes is :avatarAttributes",
                AvatarAttributes.class
            )
            .setParameter("avatarAttributes", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<AvatarAttributes> fetchAvatarCharactors(List<AvatarAttributes> avatarAttributes) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, avatarAttributes.size()).forEach(index -> order.put(avatarAttributes.get(index).getId(), index));
        List<AvatarAttributes> result = entityManager
            .createQuery(
                "select distinct avatarAttributes from AvatarAttributes avatarAttributes left join fetch avatarAttributes.avatarCharactors where avatarAttributes in :avatarAttributes",
                AvatarAttributes.class
            )
            .setParameter("avatarAttributes", avatarAttributes)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    AvatarAttributes fetchStyles(AvatarAttributes result) {
        return entityManager
            .createQuery(
                "select avatarAttributes from AvatarAttributes avatarAttributes left join fetch avatarAttributes.styles where avatarAttributes is :avatarAttributes",
                AvatarAttributes.class
            )
            .setParameter("avatarAttributes", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<AvatarAttributes> fetchStyles(List<AvatarAttributes> avatarAttributes) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, avatarAttributes.size()).forEach(index -> order.put(avatarAttributes.get(index).getId(), index));
        List<AvatarAttributes> result = entityManager
            .createQuery(
                "select distinct avatarAttributes from AvatarAttributes avatarAttributes left join fetch avatarAttributes.styles where avatarAttributes in :avatarAttributes",
                AvatarAttributes.class
            )
            .setParameter("avatarAttributes", avatarAttributes)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
