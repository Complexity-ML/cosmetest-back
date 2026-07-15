package com.example.cosmetest.presentation.controller;

import com.example.cosmetest.presentation.filter.LegacyApiDeprecationFilter;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class ApiVersioningContractTest {

    @Test
    void majorControllersExposeV1WithoutRemovingLegacyRoutes() {
        assertMappings(VolontaireController.class, "/api/volontaires", "/api/v1/volontaires");
        assertMappings(EtudeVolontaireController.class, "/api/etude-volontaires", "/api/v1/etude-volontaires");
        assertMappings(RdvController.class, "/api/rdvs", "/api/v1/rdvs");
    }

    @Test
    void legacyMajorRoutesAreMarkedDeprecated() throws Exception {
        LegacyApiDeprecationFilter filter = new LegacyApiDeprecationFilter();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/volontaires/42");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, mock(FilterChain.class));

        assertThat(response.getHeader("Deprecation")).isEqualTo("true");
        assertThat(response.getHeader("Link")).contains("/api/v1/volontaires");
    }

    @Test
    void v1RoutesAreNotMarkedDeprecated() throws Exception {
        LegacyApiDeprecationFilter filter = new LegacyApiDeprecationFilter();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/volontaires/42");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, mock(FilterChain.class));

        assertThat(response.getHeader("Deprecation")).isNull();
    }

    private static void assertMappings(Class<?> controller, String legacy, String v1) {
        RequestMapping mapping = controller.getAnnotation(RequestMapping.class);
        assertThat(Arrays.asList(mapping.value())).contains(legacy, v1);
    }
}
