package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.BooksRelatedOption;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface BooksRelatedOptionRepositoryWithBagRelationships {
    Optional<BooksRelatedOption> fetchBagRelationships(Optional<BooksRelatedOption> booksRelatedOption);

    List<BooksRelatedOption> fetchBagRelationships(List<BooksRelatedOption> booksRelatedOptions);

    Page<BooksRelatedOption> fetchBagRelationships(Page<BooksRelatedOption> booksRelatedOptions);
}
