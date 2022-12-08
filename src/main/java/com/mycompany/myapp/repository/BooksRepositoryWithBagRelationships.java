package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Books;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface BooksRepositoryWithBagRelationships {
    Optional<Books> fetchBagRelationships(Optional<Books> books);

    List<Books> fetchBagRelationships(List<Books> books);

    Page<Books> fetchBagRelationships(Page<Books> books);
}
