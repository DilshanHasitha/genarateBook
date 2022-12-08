package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PriceRelatedOptionDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PriceRelatedOptionDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PriceRelatedOptionDetailsRepository
    extends JpaRepository<PriceRelatedOptionDetails, Long>, JpaSpecificationExecutor<PriceRelatedOptionDetails> {}
