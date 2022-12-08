package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.BooksPage;
import com.mycompany.myapp.domain.LayerGroup;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BooksPage entity.
 *
 * When extending this class, extend BooksPageRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface BooksPageRepository
    extends BooksPageRepositoryWithBagRelationships, JpaRepository<BooksPage, Long>, JpaSpecificationExecutor<BooksPage> {
    default Optional<BooksPage> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<BooksPage> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<BooksPage> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }

    BooksPage findOneByNum(Integer num);
}
