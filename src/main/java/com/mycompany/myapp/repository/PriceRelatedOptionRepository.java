package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PriceRelatedOption;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PriceRelatedOption entity.
 *
 * When extending this class, extend PriceRelatedOptionRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface PriceRelatedOptionRepository
    extends
        PriceRelatedOptionRepositoryWithBagRelationships,
        JpaRepository<PriceRelatedOption, Long>,
        JpaSpecificationExecutor<PriceRelatedOption> {
    default Optional<PriceRelatedOption> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<PriceRelatedOption> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<PriceRelatedOption> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
