package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AvatarAttributesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AvatarAttributes.class);
        AvatarAttributes avatarAttributes1 = new AvatarAttributes();
        avatarAttributes1.setId(1L);
        AvatarAttributes avatarAttributes2 = new AvatarAttributes();
        avatarAttributes2.setId(avatarAttributes1.getId());
        assertThat(avatarAttributes1).isEqualTo(avatarAttributes2);
        avatarAttributes2.setId(2L);
        assertThat(avatarAttributes1).isNotEqualTo(avatarAttributes2);
        avatarAttributes1.setId(null);
        assertThat(avatarAttributes1).isNotEqualTo(avatarAttributes2);
    }
}
