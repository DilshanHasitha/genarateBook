package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Character;
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
public class CharacterRepositoryWithBagRelationshipsImpl implements CharacterRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Character> fetchBagRelationships(Optional<Character> character) {
        return character.map(this::fetchAvatarCharactors);
    }

    @Override
    public Page<Character> fetchBagRelationships(Page<Character> characters) {
        return new PageImpl<>(fetchBagRelationships(characters.getContent()), characters.getPageable(), characters.getTotalElements());
    }

    @Override
    public List<Character> fetchBagRelationships(List<Character> characters) {
        return Optional.of(characters).map(this::fetchAvatarCharactors).orElse(Collections.emptyList());
    }

    Character fetchAvatarCharactors(Character result) {
        return entityManager
            .createQuery(
                "select character from Character character left join fetch character.avatarCharactors where character is :character",
                Character.class
            )
            .setParameter("character", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Character> fetchAvatarCharactors(List<Character> characters) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, characters.size()).forEach(index -> order.put(characters.get(index).getId(), index));
        List<Character> result = entityManager
            .createQuery(
                "select distinct character from Character character left join fetch character.avatarCharactors where character in :characters",
                Character.class
            )
            .setParameter("characters", characters)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
