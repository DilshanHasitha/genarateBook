package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SelectedOption;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface SelectedOptionRepositoryWithBagRelationships {
    Optional<SelectedOption> fetchBagRelationships(Optional<SelectedOption> selectedOption);

    List<SelectedOption> fetchBagRelationships(List<SelectedOption> selectedOptions);

    Page<SelectedOption> fetchBagRelationships(Page<SelectedOption> selectedOptions);
}
