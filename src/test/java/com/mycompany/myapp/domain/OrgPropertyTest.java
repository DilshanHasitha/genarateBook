package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrgPropertyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrgProperty.class);
        OrgProperty orgProperty1 = new OrgProperty();
        orgProperty1.setId(1L);
        OrgProperty orgProperty2 = new OrgProperty();
        orgProperty2.setId(orgProperty1.getId());
        assertThat(orgProperty1).isEqualTo(orgProperty2);
        orgProperty2.setId(2L);
        assertThat(orgProperty1).isNotEqualTo(orgProperty2);
        orgProperty1.setId(null);
        assertThat(orgProperty1).isNotEqualTo(orgProperty2);
    }
}
