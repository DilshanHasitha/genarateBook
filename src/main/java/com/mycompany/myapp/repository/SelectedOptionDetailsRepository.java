package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SelectedOptionDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SelectedOptionDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SelectedOptionDetailsRepository
    extends JpaRepository<SelectedOptionDetails, Long>, JpaSpecificationExecutor<SelectedOptionDetails> {}
