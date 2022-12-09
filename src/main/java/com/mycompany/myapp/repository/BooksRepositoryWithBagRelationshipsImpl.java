package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Books;
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
public class BooksRepositoryWithBagRelationshipsImpl implements BooksRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Books> fetchBagRelationships(Optional<Books> books) {
        return books
            .map(this::fetchBooksPages)
            .map(this::fetchPriceRelatedOptions)
            .map(this::fetchBooksRelatedOptions)
            .map(this::fetchBooksAttributes)
            .map(this::fetchBooksVariables)
            .map(this::fetchAvatarAttributes)
            .map(this::fetchLayerGroups)
            .map(this::fetchSelections);
    }

    @Override
    public Page<Books> fetchBagRelationships(Page<Books> books) {
        return new PageImpl<>(fetchBagRelationships(books.getContent()), books.getPageable(), books.getTotalElements());
    }

    @Override
    public List<Books> fetchBagRelationships(List<Books> books) {
        return Optional
            .of(books)
            .map(this::fetchBooksPages)
            .map(this::fetchPriceRelatedOptions)
            .map(this::fetchBooksRelatedOptions)
            .map(this::fetchBooksAttributes)
            .map(this::fetchBooksVariables)
            .map(this::fetchAvatarAttributes)
            .map(this::fetchLayerGroups)
            .map(this::fetchSelections)
            .orElse(Collections.emptyList());
    }

    Books fetchBooksPages(Books result) {
        return entityManager
            .createQuery("select books from Books books left join fetch books.booksPages where books is :books", Books.class)
            .setParameter("books", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Books> fetchBooksPages(List<Books> books) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, books.size()).forEach(index -> order.put(books.get(index).getId(), index));
        List<Books> result = entityManager
            .createQuery("select distinct books from Books books left join fetch books.booksPages where books in :books", Books.class)
            .setParameter("books", books)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Books fetchPriceRelatedOptions(Books result) {
        return entityManager
            .createQuery("select books from Books books left join fetch books.priceRelatedOptions where books is :books", Books.class)
            .setParameter("books", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Books> fetchPriceRelatedOptions(List<Books> books) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, books.size()).forEach(index -> order.put(books.get(index).getId(), index));
        List<Books> result = entityManager
            .createQuery(
                "select distinct books from Books books left join fetch books.priceRelatedOptions where books in :books",
                Books.class
            )
            .setParameter("books", books)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Books fetchBooksRelatedOptions(Books result) {
        return entityManager
            .createQuery("select books from Books books left join fetch books.booksRelatedOptions where books is :books", Books.class)
            .setParameter("books", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Books> fetchBooksRelatedOptions(List<Books> books) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, books.size()).forEach(index -> order.put(books.get(index).getId(), index));
        List<Books> result = entityManager
            .createQuery(
                "select distinct books from Books books left join fetch books.booksRelatedOptions where books in :books",
                Books.class
            )
            .setParameter("books", books)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Books fetchBooksAttributes(Books result) {
        return entityManager
            .createQuery("select books from Books books left join fetch books.booksAttributes where books is :books", Books.class)
            .setParameter("books", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Books> fetchBooksAttributes(List<Books> books) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, books.size()).forEach(index -> order.put(books.get(index).getId(), index));
        List<Books> result = entityManager
            .createQuery("select distinct books from Books books left join fetch books.booksAttributes where books in :books", Books.class)
            .setParameter("books", books)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Books fetchBooksVariables(Books result) {
        return entityManager
            .createQuery("select books from Books books left join fetch books.booksVariables where books is :books", Books.class)
            .setParameter("books", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Books> fetchBooksVariables(List<Books> books) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, books.size()).forEach(index -> order.put(books.get(index).getId(), index));
        List<Books> result = entityManager
            .createQuery("select distinct books from Books books left join fetch books.booksVariables where books in :books", Books.class)
            .setParameter("books", books)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Books fetchAvatarAttributes(Books result) {
        return entityManager
            .createQuery("select books from Books books left join fetch books.avatarAttributes where books is :books", Books.class)
            .setParameter("books", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Books> fetchAvatarAttributes(List<Books> books) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, books.size()).forEach(index -> order.put(books.get(index).getId(), index));
        List<Books> result = entityManager
            .createQuery("select distinct books from Books books left join fetch books.avatarAttributes where books in :books", Books.class)
            .setParameter("books", books)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Books fetchLayerGroups(Books result) {
        return entityManager
            .createQuery("select books from Books books left join fetch books.layerGroups where books is :books", Books.class)
            .setParameter("books", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Books> fetchLayerGroups(List<Books> books) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, books.size()).forEach(index -> order.put(books.get(index).getId(), index));
        List<Books> result = entityManager
            .createQuery("select distinct books from Books books left join fetch books.layerGroups where books in :books", Books.class)
            .setParameter("books", books)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Books fetchSelections(Books result) {
        return entityManager
            .createQuery("select books from Books books left join fetch books.selections where books is :books", Books.class)
            .setParameter("books", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Books> fetchSelections(List<Books> books) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, books.size()).forEach(index -> order.put(books.get(index).getId(), index));
        List<Books> result = entityManager
            .createQuery("select distinct books from Books books left join fetch books.selections where books in :books", Books.class)
            .setParameter("books", books)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
