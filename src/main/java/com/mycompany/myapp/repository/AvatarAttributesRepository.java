package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.AvatarAttributes;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AvatarAttributes entity.
 *
 * When extending this class, extend AvatarAttributesRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface AvatarAttributesRepository
    extends
        AvatarAttributesRepositoryWithBagRelationships, JpaRepository<AvatarAttributes, Long>, JpaSpecificationExecutor<AvatarAttributes> {
    default Optional<AvatarAttributes> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<AvatarAttributes> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<AvatarAttributes> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select distinct avatarAttributes from AvatarAttributes avatarAttributes left join fetch avatarAttributes.optionType",
        countQuery = "select count(distinct avatarAttributes) from AvatarAttributes avatarAttributes"
    )
    Page<AvatarAttributes> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct avatarAttributes from AvatarAttributes avatarAttributes left join fetch avatarAttributes.optionType")
    List<AvatarAttributes> findAllWithToOneRelationships();

    @Query(
        "select avatarAttributes from AvatarAttributes avatarAttributes left join fetch avatarAttributes.optionType where avatarAttributes.id =:id"
    )
    Optional<AvatarAttributes> findOneWithToOneRelationships(@Param("id") Long id);
}
