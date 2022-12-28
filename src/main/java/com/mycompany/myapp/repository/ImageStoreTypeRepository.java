package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ImageStoreType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ImageStoreType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ImageStoreTypeRepository extends JpaRepository<ImageStoreType, Long>, JpaSpecificationExecutor<ImageStoreType> {}
