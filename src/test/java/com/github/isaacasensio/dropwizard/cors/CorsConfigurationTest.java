package com.github.isaacasensio.dropwizard.cors;

import com.github.isaacasensio.dropwizard.cors.CorsConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.HttpMethod;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CorsConfigurationTest {

    private CorsConfiguration configuration;

    public static final String[] DEFAULT_ALLOWED_METHODS = {
            HttpMethod.GET,
            HttpMethod.POST,
            HttpMethod.PUT,
            HttpMethod.HEAD,
            HttpMethod.OPTIONS,
            HttpMethod.DELETE,
            "PATCH"
    };

    @Before
    public void setUp() throws Exception {
        configuration = new CorsConfiguration();
    }

    @Test
    public void ensureAllOriginsAreAllowedByDefault() {
        String allDomainsAllowed = "*";
        assertThat(configuration.getAllowedOrigins(), is(allDomainsAllowed));
    }

    @Test
    public void ensureConfiguredOriginsAreSet() {
        String localhostDomain = "localhost";
        configuration.setAllowedOrigins(localhostDomain);
        assertThat(configuration.getAllowedOrigins(), is(localhostDomain));
    }


    @Test
    public void ensureDefaultAllowedHeadersArePresent() {

        List<String> defaultAllowedHeaders = asList(
                StringUtils.split(configuration.getAllowedHeaders(), ",")
        );

        assertThat(defaultAllowedHeaders,
                containsInAnyOrder(
                        "X-Requested-With",
                        "Content-Type",
                        "Accept",
                        "Origin"
                ));
    }

    @Test
    public void ensureOnlyAllowedHeadersArePresent() {

        String contentTypeHeader = "Content-Type";
        configuration.setAllowedHeaders(contentTypeHeader);

        assertThat(configuration.getAllowedHeaders(), is(contentTypeHeader));
    }

    @Test
    public void ensureDefaultMappingIsAssigned() {
        String defaultUrlMapping = "/*";
        assertThat(configuration.getUrlMapping(), is(defaultUrlMapping));
    }

    @Test
    public void ensureCustomMappingIsAssigned() {

        String customUrlMapping = "/api/*";
        configuration.setUrlMapping(customUrlMapping);

        assertThat(configuration.getUrlMapping(), is(customUrlMapping));
    }

    @Test
    public void ensureAllMethodsAreAllowedByDefault() {

        List<String> defaultAllowedMethods = asList(
                StringUtils.split(configuration.getAllowedMethods(), ",")
        );

        assertThat(defaultAllowedMethods, containsInAnyOrder(DEFAULT_ALLOWED_METHODS));
    }


    @Test
    public void ensureOnlyConfiguredMethodsAreAllowed() {

        String[] configuredMethods = {HttpMethod.GET, HttpMethod.DELETE};

        configuration.setAllowedMethods(
                StringUtils.join(configuredMethods, ",")
        );

        List<String> configuredMethodList = asList(
                StringUtils.split(configuration.getAllowedMethods(), ",")
        );

        assertThat(configuredMethodList, not(containsInAnyOrder(DEFAULT_ALLOWED_METHODS)));

        assertThat(configuredMethodList, containsInAnyOrder(configuredMethods));
    }


    @Test
    public void ensurePreflightMaxAgeIsConfiguredByDefaultIn30Minutes() {
        assertThat(configuration.getMaxAgeInSeconds(), is(TimeUnit.MINUTES.toSeconds(30)));
    }


    @Test
    public void preflightMaxAgeCanBeConfigured() {
        configuration.setMaxAgeInSeconds(TimeUnit.MINUTES.toSeconds(10));

        assertThat(configuration.getMaxAgeInSeconds(), is(TimeUnit.MINUTES.toSeconds(10)));
    }
}