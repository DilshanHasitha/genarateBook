package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.BooksVariables;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BooksVariables entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BooksVariablesRepository extends JpaRepository<BooksVariables, Long>, JpaSpecificationExecutor<BooksVariables> {}
