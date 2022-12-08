package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Styles;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Styles entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StylesRepository extends JpaRepository<Styles, Long>, JpaSpecificationExecutor<Styles> {}
