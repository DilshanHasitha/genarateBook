package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Character;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface CharacterRepositoryWithBagRelationships {
    Optional<Character> fetchBagRelationships(Optional<Character> character);

    List<Character> fetchBagRelationships(List<Character> characters);

    Page<Character> fetchBagRelationships(Page<Character> characters);
}
