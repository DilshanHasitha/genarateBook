package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Character;
import com.mycompany.myapp.repository.CharacterRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CharacterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CharacterResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/characters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCharacterMockMvc;

    private Character character;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Character createEntity(EntityManager em) {
        Character character = new Character().code(DEFAULT_CODE).description(DEFAULT_DESCRIPTION).isActive(DEFAULT_IS_ACTIVE);
        return character;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Character createUpdatedEntity(EntityManager em) {
        Character character = new Character().code(UPDATED_CODE).description(UPDATED_DESCRIPTION).isActive(UPDATED_IS_ACTIVE);
        return character;
    }

    @BeforeEach
    public void initTest() {
        character = createEntity(em);
    }

    @Test
    @Transactional
    void createCharacter() throws Exception {
        int databaseSizeBeforeCreate = characterRepository.findAll().size();
        // Create the Character
        restCharacterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(character)))
            .andExpect(status().isCreated());

        // Validate the Character in the database
        List<Character> characterList = characterRepository.findAll();
        assertThat(characterList).hasSize(databaseSizeBeforeCreate + 1);
        Character testCharacter = characterList.get(characterList.size() - 1);
        assertThat(testCharacter.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCharacter.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCharacter.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createCharacterWithExistingId() throws Exception {
        // Create the Character with an existing ID
        character.setId(1L);

        int databaseSizeBeforeCreate = characterRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCharacterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(character)))
            .andExpect(status().isBadRequest());

        // Validate the Character in the database
        List<Character> characterList = characterRepository.findAll();
        assertThat(characterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCharacters() throws Exception {
        // Initialize the database
        characterRepository.saveAndFlush(character);

        // Get all the characterList
        restCharacterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(character.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getCharacter() throws Exception {
        // Initialize the database
        characterRepository.saveAndFlush(character);

        // Get the character
        restCharacterMockMvc
            .perform(get(ENTITY_API_URL_ID, character.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(character.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingCharacter() throws Exception {
        // Get the character
        restCharacterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCharacter() throws Exception {
        // Initialize the database
        characterRepository.saveAndFlush(character);

        int databaseSizeBeforeUpdate = characterRepository.findAll().size();

        // Update the character
        Character updatedCharacter = characterRepository.findById(character.getId()).get();
        // Disconnect from session so that the updates on updatedCharacter are not directly saved in db
        em.detach(updatedCharacter);
        updatedCharacter.code(UPDATED_CODE).description(UPDATED_DESCRIPTION).isActive(UPDATED_IS_ACTIVE);

        restCharacterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCharacter.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCharacter))
            )
            .andExpect(status().isOk());

        // Validate the Character in the database
        List<Character> characterList = characterRepository.findAll();
        assertThat(characterList).hasSize(databaseSizeBeforeUpdate);
        Character testCharacter = characterList.get(characterList.size() - 1);
        assertThat(testCharacter.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCharacter.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCharacter.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingCharacter() throws Exception {
        int databaseSizeBeforeUpdate = characterRepository.findAll().size();
        character.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCharacterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, character.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(character))
            )
            .andExpect(status().isBadRequest());

        // Validate the Character in the database
        List<Character> characterList = characterRepository.findAll();
        assertThat(characterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCharacter() throws Exception {
        int databaseSizeBeforeUpdate = characterRepository.findAll().size();
        character.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCharacterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(character))
            )
            .andExpect(status().isBadRequest());

        // Validate the Character in the database
        List<Character> characterList = characterRepository.findAll();
        assertThat(characterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCharacter() throws Exception {
        int databaseSizeBeforeUpdate = characterRepository.findAll().size();
        character.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCharacterMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(character)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Character in the database
        List<Character> characterList = characterRepository.findAll();
        assertThat(characterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCharacterWithPatch() throws Exception {
        // Initialize the database
        characterRepository.saveAndFlush(character);

        int databaseSizeBeforeUpdate = characterRepository.findAll().size();

        // Update the character using partial update
        Character partialUpdatedCharacter = new Character();
        partialUpdatedCharacter.setId(character.getId());

        restCharacterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCharacter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCharacter))
            )
            .andExpect(status().isOk());

        // Validate the Character in the database
        List<Character> characterList = characterRepository.findAll();
        assertThat(characterList).hasSize(databaseSizeBeforeUpdate);
        Character testCharacter = characterList.get(characterList.size() - 1);
        assertThat(testCharacter.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCharacter.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCharacter.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateCharacterWithPatch() throws Exception {
        // Initialize the database
        characterRepository.saveAndFlush(character);

        int databaseSizeBeforeUpdate = characterRepository.findAll().size();

        // Update the character using partial update
        Character partialUpdatedCharacter = new Character();
        partialUpdatedCharacter.setId(character.getId());

        partialUpdatedCharacter.code(UPDATED_CODE).description(UPDATED_DESCRIPTION).isActive(UPDATED_IS_ACTIVE);

        restCharacterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCharacter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCharacter))
            )
            .andExpect(status().isOk());

        // Validate the Character in the database
        List<Character> characterList = characterRepository.findAll();
        assertThat(characterList).hasSize(databaseSizeBeforeUpdate);
        Character testCharacter = characterList.get(characterList.size() - 1);
        assertThat(testCharacter.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCharacter.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCharacter.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingCharacter() throws Exception {
        int databaseSizeBeforeUpdate = characterRepository.findAll().size();
        character.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCharacterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, character.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(character))
            )
            .andExpect(status().isBadRequest());

        // Validate the Character in the database
        List<Character> characterList = characterRepository.findAll();
        assertThat(characterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCharacter() throws Exception {
        int databaseSizeBeforeUpdate = characterRepository.findAll().size();
        character.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCharacterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(character))
            )
            .andExpect(status().isBadRequest());

        // Validate the Character in the database
        List<Character> characterList = characterRepository.findAll();
        assertThat(characterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCharacter() throws Exception {
        int databaseSizeBeforeUpdate = characterRepository.findAll().size();
        character.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCharacterMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(character))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Character in the database
        List<Character> characterList = characterRepository.findAll();
        assertThat(characterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCharacter() throws Exception {
        // Initialize the database
        characterRepository.saveAndFlush(character);

        int databaseSizeBeforeDelete = characterRepository.findAll().size();

        // Delete the character
        restCharacterMockMvc
            .perform(delete(ENTITY_API_URL_ID, character.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Character> characterList = characterRepository.findAll();
        assertThat(characterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
