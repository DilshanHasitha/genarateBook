package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Books;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Books entity.
 *
 * When extending this class, extend BooksRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface BooksRepository extends BooksRepositoryWithBagRelationships, JpaRepository<Books, Long>, JpaSpecificationExecutor<Books> {
    @Query("select books from Books books where books.user.login = ?#{principal.username}")
    List<Books> findByUserIsCurrentUser();

    default Optional<Books> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Books> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Books> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }

    Optional<Books> findOneByCode(String Code);
}
