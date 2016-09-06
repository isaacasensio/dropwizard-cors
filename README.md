A library  supports in [Dropwizard](http://dropwizard.io/) applications.


A bundle that provides first class support for [CORS] (https://en.wikipedia.org/wiki/Cross-origin_resource_sharing) in Dropwizard applications.

Dependency Info
---------------
```xml
<dependency>
    <groupId>com.github.isaacasensio.dropwizard</groupId>
    <artifactId>dropwizard-cors</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```


Usage
-----
Add a `CorsBundle` to your [Application](http://www.dropwizard.io/1.0.0/dropwizard-core/apidocs/io/dropwizard/Application.html) class.

```java
@Override
public void initialize(Bootstrap<MyConfiguration> bootstrap) {
    // ...
    bootstrap.addBundle(new CorsBundle());
}
```


Implement `CorsBundleConfiguration` in your [Configuration](http://www.dropwizard.io/1.0.0/dropwizard-core/apidocs/io/dropwizard/Configuration.html) class.


```java
public class MyConfiguration extends Configuration implements CorsBundleConfiguration{

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
```

Configuration
-------------
For configuring the CORS bundle, there is a `CorsConfiguration`:

```yaml
corsConfiguration:
  # Optional: Supported source origins
  allowedOrigins: *
  # Optional: Supported headers
  allowedHeaders: X-Requested-With,Content-Type,Accept,Origin
  # Optional: Supported methods
  allowedMethods: OPTIONS,GET,PUT,POST,DELETE,HEAD,PATCH
  # Optional: Indicates that this preflight response is good for 30 minutes, after which a new preflight request must be issued.
  preflightMaxAge: 1800
  # Optional: URL mapping where CORS filter will be applied
  urlMapping: /*
