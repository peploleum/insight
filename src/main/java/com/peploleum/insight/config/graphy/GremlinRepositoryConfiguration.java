package com.peploleum.insight.config.graphy;

import com.microsoft.spring.data.gremlin.common.GremlinConfig;
import com.microsoft.spring.data.gremlin.config.AbstractGremlinConfiguration;
import com.microsoft.spring.data.gremlin.repository.config.EnableGremlinRepositories;
import io.github.jhipster.config.JHipsterConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Created by GFOLGOAS on 29/03/2019.
 */
@Configuration
@EnableGremlinRepositories(basePackages = "com.peploleum.insight.repository.graphy")
@EnableConfigurationProperties(GremlinProperties.class)
@Profile("graphy")
public class GremlinRepositoryConfiguration extends AbstractGremlinConfiguration {
    @Autowired
    private GremlinProperties gremlinProps;
    @Value("${endpoint}")
    private String enpoint;
    @Value("${port}")
    private String port;

    @Override
    public GremlinConfig getGremlinConfig() {
        final GremlinConfig gremlinConfiguration = new GremlinConfig(enpoint, Integer.valueOf(port).intValue(),
            gremlinProps.getUsername(), gremlinProps.getPassword(), false, false, gremlinProps.getSerializer());
        return gremlinConfiguration;
    }
}
