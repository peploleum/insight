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
    private final ApplicationProperties.Graphy graphy = new ApplicationProperties.Graphy();

    public ApplicationProperties() {
    }

    public ApplicationProperties.Kibana getKibana() {
        return this.kibana;
    }

    public ApplicationProperties.Graphy getGraphy() {
        return this.graphy;
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


    public static class Graphy {
        private String host = "localhost";
        private String port = "8090";
        private boolean enabled = false;
        private Integer vertexThreshold = 3;

        public Graphy() {
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getPort() {
            return port;
        }

        public void setPort(String port) {
            this.port = port;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public Integer getVertexThreshold() {
            return vertexThreshold;
        }

        public void setVertexThreshold(Integer vertexThreshold) {
            this.vertexThreshold = vertexThreshold;
        }
    }
}
