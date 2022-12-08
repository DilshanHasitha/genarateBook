package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Layers;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface LayersRepositoryWithBagRelationships {
    Optional<Layers> fetchBagRelationships(Optional<Layers> layers);

    List<Layers> fetchBagRelationships(List<Layers> layers);

    Page<Layers> fetchBagRelationships(Page<Layers> layers);
}
