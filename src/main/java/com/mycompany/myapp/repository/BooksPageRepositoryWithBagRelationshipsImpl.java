package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.BooksPage;
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
public class BooksPageRepositoryWithBagRelationshipsImpl implements BooksPageRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<BooksPage> fetchBagRelationships(Optional<BooksPage> booksPage) {
        return booksPage.map(this::fetchPageDetails);
    }

    @Override
    public Page<BooksPage> fetchBagRelationships(Page<BooksPage> booksPages) {
        return new PageImpl<>(fetchBagRelationships(booksPages.getContent()), booksPages.getPageable(), booksPages.getTotalElements());
    }

    @Override
    public List<BooksPage> fetchBagRelationships(List<BooksPage> booksPages) {
        return Optional.of(booksPages).map(this::fetchPageDetails).orElse(Collections.emptyList());
    }

    BooksPage fetchPageDetails(BooksPage result) {
        return entityManager
            .createQuery(
                "select booksPage from BooksPage booksPage left join fetch booksPage.pageDetails where booksPage is :booksPage",
                BooksPage.class
            )
            .setParameter("booksPage", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<BooksPage> fetchPageDetails(List<BooksPage> booksPages) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, booksPages.size()).forEach(index -> order.put(booksPages.get(index).getId(), index));
        List<BooksPage> result = entityManager
            .createQuery(
                "select distinct booksPage from BooksPage booksPage left join fetch booksPage.pageDetails where booksPage in :booksPages",
                BooksPage.class
            )
            .setParameter("booksPages", booksPages)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
