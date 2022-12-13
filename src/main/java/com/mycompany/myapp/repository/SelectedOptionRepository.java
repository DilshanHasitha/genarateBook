package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SelectedOption;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SelectedOption entity.
 *
 * When extending this class, extend SelectedOptionRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface SelectedOptionRepository
    extends SelectedOptionRepositoryWithBagRelationships, JpaRepository<SelectedOption, Long>, JpaSpecificationExecutor<SelectedOption> {
    default Optional<SelectedOption> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<SelectedOption> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<SelectedOption> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }

    SelectedOption findOneByCodeAndBooks_Code(String Code, String bookCode);
}
