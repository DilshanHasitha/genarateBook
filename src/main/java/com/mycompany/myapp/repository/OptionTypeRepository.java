package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.OptionType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OptionType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OptionTypeRepository extends JpaRepository<OptionType, Long>, JpaSpecificationExecutor<OptionType> {}
