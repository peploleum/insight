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

    public ApplicationProperties() {
    }

    public ApplicationProperties.Kibana getKibana() {
        return this.kibana;
    }

    public static class Kibana {
        private String uri = "http://192.168.99.100:5601/";

        public Kibana() {
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }
    }
}
