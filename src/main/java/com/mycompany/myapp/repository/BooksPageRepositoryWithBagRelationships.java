package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.BooksPage;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface BooksPageRepositoryWithBagRelationships {
    Optional<BooksPage> fetchBagRelationships(Optional<BooksPage> booksPage);

    List<BooksPage> fetchBagRelationships(List<BooksPage> booksPages);

    Page<BooksPage> fetchBagRelationships(Page<BooksPage> booksPages);
}
