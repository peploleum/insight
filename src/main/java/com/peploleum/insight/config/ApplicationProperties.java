package com.peploleum.insight.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Insight.
 * <p>
 * Properties are configured in the application.yml file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
    private final ApplicationProperties.Kibana kibana = new ApplicationProperties.Kibana();
    private final ApplicationProperties.Houston houston = new ApplicationProperties.Houston();

    public ApplicationProperties() {
    }

    public ApplicationProperties.Kibana getKibana() {
        return this.kibana;
    }

    public ApplicationProperties.Houston getHouston() {
        return houston;
    }

    public static class Kibana {
        private String uri = "http://localhost:5601/";

        public Kibana() {
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }
    }

    public static class Houston {
        private String uri = "http://localhost:8090/";

        public Houston() {
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }
    }
}
