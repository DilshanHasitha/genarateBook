package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.LayerDetails;
import com.mycompany.myapp.domain.LayerGroup;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the LayerGroup entity.
 *
 * When extending this class, extend LayerGroupRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface LayerGroupRepository
    extends LayerGroupRepositoryWithBagRelationships, JpaRepository<LayerGroup, Long>, JpaSpecificationExecutor<LayerGroup> {
    default Optional<LayerGroup> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<LayerGroup> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<LayerGroup> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }

    LayerGroup findOneByCode(String code);
}
