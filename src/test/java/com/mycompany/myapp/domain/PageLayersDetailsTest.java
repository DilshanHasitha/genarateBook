package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PageLayersDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PageLayersDetails.class);
        PageLayersDetails pageLayersDetails1 = new PageLayersDetails();
        pageLayersDetails1.setId(1L);
        PageLayersDetails pageLayersDetails2 = new PageLayersDetails();
        pageLayersDetails2.setId(pageLayersDetails1.getId());
        assertThat(pageLayersDetails1).isEqualTo(pageLayersDetails2);
        pageLayersDetails2.setId(2L);
        assertThat(pageLayersDetails1).isNotEqualTo(pageLayersDetails2);
        pageLayersDetails1.setId(null);
        assertThat(pageLayersDetails1).isNotEqualTo(pageLayersDetails2);
    }
}
