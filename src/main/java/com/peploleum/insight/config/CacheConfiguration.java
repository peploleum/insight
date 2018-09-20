package com.peploleum.insight.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import io.github.jhipster.config.jcache.BeanClassLoaderAwareJCacheRegionFactory;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        BeanClassLoaderAwareJCacheRegionFactory.setBeanClassLoader(this.getClass().getClassLoader());
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(com.peploleum.insight.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(com.peploleum.insight.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.Biographics.class.getName(), jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.Biographics.class.getName() + ".events", jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.Biographics.class.getName() + ".equipment", jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.Biographics.class.getName() + ".locations", jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.Biographics.class.getName() + ".organisations", jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.Organisation.class.getName(), jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.Organisation.class.getName() + ".locations", jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.Organisation.class.getName() + ".biographics", jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.Organisation.class.getName() + ".events", jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.Organisation.class.getName() + ".equipment", jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.Location.class.getName(), jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.Location.class.getName() + ".biographics", jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.Location.class.getName() + ".events", jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.Location.class.getName() + ".equipment", jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.Location.class.getName() + ".organisations", jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.Event.class.getName(), jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.Event.class.getName() + ".equipment", jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.Event.class.getName() + ".locations", jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.Event.class.getName() + ".organisations", jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.Event.class.getName() + ".biographics", jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.Equipment.class.getName(), jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.Equipment.class.getName() + ".locations", jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.Equipment.class.getName() + ".organisations", jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.Equipment.class.getName() + ".biographics", jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.Equipment.class.getName() + ".events", jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
