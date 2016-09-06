package com.github.isaacasensio.dropwizard.cors.support;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.isaacasensio.dropwizard.cors.CorsBundleConfiguration;
import com.github.isaacasensio.dropwizard.cors.CorsConfiguration;
import io.dropwizard.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class ApplicationConfiguration extends Configuration implements CorsBundleConfiguration{

    @Valid
    @NotNull
    private CorsConfiguration corsConfiguration = new CorsConfiguration();


    @JsonProperty("corsConfiguration")
    @Override
    public CorsConfiguration getCorsConfiguration() {
        return corsConfiguration;
    }

    @JsonProperty("corsConfiguration")
    public void setCorsConfiguration(CorsConfiguration corsConfiguration) {
        this.corsConfiguration = corsConfiguration;
    }
}
