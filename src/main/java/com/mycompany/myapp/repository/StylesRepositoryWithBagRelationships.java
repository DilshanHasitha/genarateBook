package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Styles;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface StylesRepositoryWithBagRelationships {
    Optional<Styles> fetchBagRelationships(Optional<Styles> styles);

    List<Styles> fetchBagRelationships(List<Styles> styles);

    Page<Styles> fetchBagRelationships(Page<Styles> styles);
}
