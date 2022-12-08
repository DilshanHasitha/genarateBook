package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.BooksOptionDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BooksOptionDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BooksOptionDetailsRepository
    extends JpaRepository<BooksOptionDetails, Long>, JpaSpecificationExecutor<BooksOptionDetails> {}
