package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.AvatarAttributes;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface AvatarAttributesRepositoryWithBagRelationships {
    Optional<AvatarAttributes> fetchBagRelationships(Optional<AvatarAttributes> avatarAttributes);

    List<AvatarAttributes> fetchBagRelationships(List<AvatarAttributes> avatarAttributes);

    Page<AvatarAttributes> fetchBagRelationships(Page<AvatarAttributes> avatarAttributes);
}
