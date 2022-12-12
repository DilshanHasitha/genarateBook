package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.StylesDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StylesDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StylesDetailsRepository extends JpaRepository<StylesDetails, Long> {}
