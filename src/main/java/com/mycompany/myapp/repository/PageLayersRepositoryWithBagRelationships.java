package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PageLayers;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface PageLayersRepositoryWithBagRelationships {
    Optional<PageLayers> fetchBagRelationships(Optional<PageLayers> pageLayers);

    List<PageLayers> fetchBagRelationships(List<PageLayers> pageLayers);

    Page<PageLayers> fetchBagRelationships(Page<PageLayers> pageLayers);
}
