package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AvatarCharactorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AvatarCharactor.class);
        AvatarCharactor avatarCharactor1 = new AvatarCharactor();
        avatarCharactor1.setId(1L);
        AvatarCharactor avatarCharactor2 = new AvatarCharactor();
        avatarCharactor2.setId(avatarCharactor1.getId());
        assertThat(avatarCharactor1).isEqualTo(avatarCharactor2);
        avatarCharactor2.setId(2L);
        assertThat(avatarCharactor1).isNotEqualTo(avatarCharactor2);
        avatarCharactor1.setId(null);
        assertThat(avatarCharactor1).isNotEqualTo(avatarCharactor2);
    }
}
