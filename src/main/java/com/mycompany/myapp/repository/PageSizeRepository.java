package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PageSize;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PageSize entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PageSizeRepository extends JpaRepository<PageSize, Long>, JpaSpecificationExecutor<PageSize> {}
