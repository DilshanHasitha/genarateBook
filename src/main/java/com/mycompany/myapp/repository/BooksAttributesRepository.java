package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.BooksAttributes;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BooksAttributes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BooksAttributesRepository extends JpaRepository<BooksAttributes, Long>, JpaSpecificationExecutor<BooksAttributes> {}
