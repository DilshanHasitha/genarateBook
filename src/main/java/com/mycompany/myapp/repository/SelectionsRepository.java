package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SelectedOption;
import com.mycompany.myapp.domain.Selections;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Selections entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SelectionsRepository extends JpaRepository<Selections, Long> {
    Selections findOneByAvatarCodeAndStyleCodeAndOptionCode(String avatarCode, String styleCode, String optionCode);
}
