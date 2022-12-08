package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.BooksRelatedOption;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BooksRelatedOption entity.
 *
 * When extending this class, extend BooksRelatedOptionRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface BooksRelatedOptionRepository
    extends
        BooksRelatedOptionRepositoryWithBagRelationships,
        JpaRepository<BooksRelatedOption, Long>,
        JpaSpecificationExecutor<BooksRelatedOption> {
    default Optional<BooksRelatedOption> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<BooksRelatedOption> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<BooksRelatedOption> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
