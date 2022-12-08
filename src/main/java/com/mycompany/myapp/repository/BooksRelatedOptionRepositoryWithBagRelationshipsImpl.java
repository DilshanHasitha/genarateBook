package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.BooksRelatedOption;
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
public class BooksRelatedOptionRepositoryWithBagRelationshipsImpl implements BooksRelatedOptionRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<BooksRelatedOption> fetchBagRelationships(Optional<BooksRelatedOption> booksRelatedOption) {
        return booksRelatedOption.map(this::fetchBooksRelatedOptionDetails);
    }

    @Override
    public Page<BooksRelatedOption> fetchBagRelationships(Page<BooksRelatedOption> booksRelatedOptions) {
        return new PageImpl<>(
            fetchBagRelationships(booksRelatedOptions.getContent()),
            booksRelatedOptions.getPageable(),
            booksRelatedOptions.getTotalElements()
        );
    }

    @Override
    public List<BooksRelatedOption> fetchBagRelationships(List<BooksRelatedOption> booksRelatedOptions) {
        return Optional.of(booksRelatedOptions).map(this::fetchBooksRelatedOptionDetails).orElse(Collections.emptyList());
    }

    BooksRelatedOption fetchBooksRelatedOptionDetails(BooksRelatedOption result) {
        return entityManager
            .createQuery(
                "select booksRelatedOption from BooksRelatedOption booksRelatedOption left join fetch booksRelatedOption.booksRelatedOptionDetails where booksRelatedOption is :booksRelatedOption",
                BooksRelatedOption.class
            )
            .setParameter("booksRelatedOption", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<BooksRelatedOption> fetchBooksRelatedOptionDetails(List<BooksRelatedOption> booksRelatedOptions) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, booksRelatedOptions.size()).forEach(index -> order.put(booksRelatedOptions.get(index).getId(), index));
        List<BooksRelatedOption> result = entityManager
            .createQuery(
                "select distinct booksRelatedOption from BooksRelatedOption booksRelatedOption left join fetch booksRelatedOption.booksRelatedOptionDetails where booksRelatedOption in :booksRelatedOptions",
                BooksRelatedOption.class
            )
            .setParameter("booksRelatedOptions", booksRelatedOptions)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
