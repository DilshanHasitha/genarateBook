package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PageLayersDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PageLayersDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PageLayersDetailsRepository extends JpaRepository<PageLayersDetails, Long>, JpaSpecificationExecutor<PageLayersDetails> {}
