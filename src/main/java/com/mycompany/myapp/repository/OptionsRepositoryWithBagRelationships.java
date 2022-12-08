package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Options;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface OptionsRepositoryWithBagRelationships {
    Optional<Options> fetchBagRelationships(Optional<Options> options);

    List<Options> fetchBagRelationships(List<Options> options);

    Page<Options> fetchBagRelationships(Page<Options> options);
}
