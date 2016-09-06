package com.github.isaacasensio.dropwizard.cors;

import com.github.isaacasensio.dropwizard.cors.support.ApplicationConfiguration;
import com.github.isaacasensio.dropwizard.cors.support.MyApplication;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.apache.http.HttpStatus;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import java.util.UUID;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.hamcrest.collection.IsMapContaining.hasKey;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PreflightRequestTest {

    @ClassRule
    public static final DropwizardAppRule<ApplicationConfiguration> RULE =
            new DropwizardAppRule<>(MyApplication.class, ResourceHelpers.resourceFilePath("config.yaml"));

    private static final String ORIGIN_HEADER = "Origin";
    private static final String NOT_ALLOWED_DOMAIN = "http://third.party.domain.com";

    private Client client;
    private CorsConfiguration corsConfiguration;

    @Before
    public void setup(){
        client = new JerseyClientBuilder(RULE.getEnvironment()).build(UUID.randomUUID().toString());
        corsConfiguration = RULE.getConfiguration().getCorsConfiguration();
    }

    @Test
    public void shouldNotReturnAccessControlHeadersWhenOriginIsNotAllowed() {

        Response response = client.target(getURL("with-cors"))
                .request()
                .header(ORIGIN_HEADER, NOT_ALLOWED_DOMAIN)
                .header(CrossOriginFilter.ACCESS_CONTROL_REQUEST_METHOD_HEADER, HttpMethod.GET)
                .header(CrossOriginFilter.ACCESS_CONTROL_REQUEST_HEADERS_HEADER, ORIGIN_HEADER)
                .options();

        assertThat(response.getStatus(), is(HttpStatus.SC_OK));
        assertThat(response.getHeaders(), not(hasKey(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER)));
        assertThat(response.getHeaders(), not(hasKey(CrossOriginFilter.ACCESS_CONTROL_ALLOW_HEADERS_HEADER)));
        assertThat(response.getHeaders(), not(hasKey(CrossOriginFilter.ACCESS_CONTROL_ALLOW_METHODS_HEADER)));
        assertThat(response.getHeaders(), not(hasKey(CrossOriginFilter.ACCESS_CONTROL_ALLOW_CREDENTIALS_HEADER)));
    }

    @Test
    public void shouldResponseWithAllowedWhenOriginMatches() {

        Response response = client.target(getURL("with-cors"))
                .request()
                .header(ORIGIN_HEADER, corsConfiguration.getAllowedOrigins())
                .header(CrossOriginFilter.ACCESS_CONTROL_REQUEST_METHOD_HEADER, HttpMethod.GET)
                .header(CrossOriginFilter.ACCESS_CONTROL_REQUEST_HEADERS_HEADER, ORIGIN_HEADER)
                .options();

        assertThat(response.getStatus(), is(HttpStatus.SC_OK));

        assertThat(response.getHeaders(), hasEntry(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER,
                                                    singletonList(corsConfiguration.getAllowedOrigins())));

        assertThat(response.getHeaders(), hasEntry(CrossOriginFilter.ACCESS_CONTROL_ALLOW_HEADERS_HEADER,
                                                    singletonList(corsConfiguration.getAllowedHeaders())));

        assertThat(response.getHeaders(), hasEntry(CrossOriginFilter.ACCESS_CONTROL_ALLOW_METHODS_HEADER,
                                                    singletonList(corsConfiguration.getAllowedMethods())));

        assertThat(response.getHeaders(), hasKey(CrossOriginFilter.ACCESS_CONTROL_MAX_AGE_HEADER));

    }

    @Test
    public void onlyMatchingUrlsWillReceiveCorsHeaders() {

        Response response = client.target(getURL("without-cors"))
                .request()
                .header(ORIGIN_HEADER, corsConfiguration.getAllowedOrigins())
                .header(CrossOriginFilter.ACCESS_CONTROL_REQUEST_METHOD_HEADER, HttpMethod.GET)
                .header(CrossOriginFilter.ACCESS_CONTROL_REQUEST_HEADERS_HEADER, ORIGIN_HEADER)
                .options();

        assertThat(response.getStatus(), is(HttpStatus.SC_OK));
        assertThat(response.getHeaders(), not(hasKey(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER)));
        assertThat(response.getHeaders(), not(hasKey(CrossOriginFilter.ACCESS_CONTROL_ALLOW_HEADERS_HEADER)));
        assertThat(response.getHeaders(), not(hasKey(CrossOriginFilter.ACCESS_CONTROL_ALLOW_METHODS_HEADER)));
        assertThat(response.getHeaders(), not(hasKey(CrossOriginFilter.ACCESS_CONTROL_ALLOW_CREDENTIALS_HEADER)));
    }

    private String getURL(String uri) {
        return String.format("http://localhost:%d/%s", RULE.getLocalPort(), uri);
    }

}
