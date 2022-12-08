package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.BooksRelatedOptionDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BooksRelatedOptionDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BooksRelatedOptionDetailsRepository
    extends JpaRepository<BooksRelatedOptionDetails, Long>, JpaSpecificationExecutor<BooksRelatedOptionDetails> {}
