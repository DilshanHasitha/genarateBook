package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.LayerGroup;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface LayerGroupRepositoryWithBagRelationships {
    Optional<LayerGroup> fetchBagRelationships(Optional<LayerGroup> layerGroup);

    List<LayerGroup> fetchBagRelationships(List<LayerGroup> layerGroups);

    Page<LayerGroup> fetchBagRelationships(Page<LayerGroup> layerGroups);
}
