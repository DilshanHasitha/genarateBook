package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.FontFamily;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FontFamily entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FontFamilyRepository extends JpaRepository<FontFamily, Long> {}
