package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.LayerDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the LayerDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LayerDetailsRepository extends JpaRepository<LayerDetails, Long>, JpaSpecificationExecutor<LayerDetails> {}
