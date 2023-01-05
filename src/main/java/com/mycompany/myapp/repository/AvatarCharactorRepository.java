package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.AvatarCharactor;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AvatarCharactor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AvatarCharactorRepository extends JpaRepository<AvatarCharactor, Long>, JpaSpecificationExecutor<AvatarCharactor> {
    Optional<Set<AvatarCharactor>> findAllByCharacter_Code(String code);
}
