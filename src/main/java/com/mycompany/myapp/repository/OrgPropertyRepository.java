package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.OrgProperty;
import java.util.Optional;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OrgProperty entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrgPropertyRepository extends JpaRepository<OrgProperty, Long> {
    Optional<OrgProperty> findOneByName(String name);
}
