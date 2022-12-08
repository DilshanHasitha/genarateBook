package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PriceRelatedOption;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface PriceRelatedOptionRepositoryWithBagRelationships {
    Optional<PriceRelatedOption> fetchBagRelationships(Optional<PriceRelatedOption> priceRelatedOption);

    List<PriceRelatedOption> fetchBagRelationships(List<PriceRelatedOption> priceRelatedOptions);

    Page<PriceRelatedOption> fetchBagRelationships(Page<PriceRelatedOption> priceRelatedOptions);
}
