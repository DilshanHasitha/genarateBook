package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LayersTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Layers.class);
        Layers layers1 = new Layers();
        layers1.setId(1L);
        Layers layers2 = new Layers();
        layers2.setId(layers1.getId());
        assertThat(layers1).isEqualTo(layers2);
        layers2.setId(2L);
        assertThat(layers1).isNotEqualTo(layers2);
        layers1.setId(null);
        assertThat(layers1).isNotEqualTo(layers2);
    }
}
