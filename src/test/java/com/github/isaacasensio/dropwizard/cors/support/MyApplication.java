package com.github.isaacasensio.dropwizard.cors.support;

import com.github.isaacasensio.dropwizard.cors.CorsBundle;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

public class MyApplication extends Application<ApplicationConfiguration> {

    @Override
    public void initialize(Bootstrap<ApplicationConfiguration> bootstrap) {
        super.initialize(bootstrap);
        bootstrap.addBundle(new CorsBundle());
    }

    @Override
    public void run(ApplicationConfiguration applicationConfiguration, Environment environment) throws Exception {
        environment.jersey().register(new TestResource());
    }


    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public class TestResource {

        @Path("with-cors")
        @GET
        public String withCors() {
            return "With CORS!";
        }

        @Path("without-cors")
        @GET
        public String withOutCors() {
            return "No CORS!";
        }

    }
}
