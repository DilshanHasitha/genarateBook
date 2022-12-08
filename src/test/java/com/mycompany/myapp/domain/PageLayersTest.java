package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PageLayersTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PageLayers.class);
        PageLayers pageLayers1 = new PageLayers();
        pageLayers1.setId(1L);
        PageLayers pageLayers2 = new PageLayers();
        pageLayers2.setId(pageLayers1.getId());
        assertThat(pageLayers1).isEqualTo(pageLayers2);
        pageLayers2.setId(2L);
        assertThat(pageLayers1).isNotEqualTo(pageLayers2);
        pageLayers1.setId(null);
        assertThat(pageLayers1).isNotEqualTo(pageLayers2);
    }
}
