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
            cm.createCache(com.peploleum.insight.domain.AttackPattern.class.getName(), jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.AttackPattern.class.getName() + ".usesAttackPatternToMalwares", jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.Campaign.class.getName(), jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.CourseOfAction.class.getName(), jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.Actor.class.getName(), jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.Actor.class.getName() + ".targetsActorToIntrusionSets", jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.Actor.class.getName() + ".targetsActorToMalwares", jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.ActivityPattern.class.getName(), jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.IntrusionSet.class.getName(), jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.IntrusionSet.class.getName() + ".isUsesIntrusionSetToTools", jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.Malware.class.getName(), jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.Malware.class.getName() + ".isVariantOfs", jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.Malware.class.getName() + ".usesMalwareToThreatActors", jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.Malware.class.getName() + ".usesMalwareToTools", jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.NetLink.class.getName(), jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.NetLink.class.getName() + ".isLinkOfAttackPatterns", jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.NetLink.class.getName() + ".isLinkOfCampaigns", jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.NetLink.class.getName() + ".isLinkOfCourseOfActions", jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.NetLink.class.getName() + ".isLinkOfActors", jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.NetLink.class.getName() + ".isLinkOfActivityPatterns", jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.NetLink.class.getName() + ".isLinkOfIntrusionSets", jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.NetLink.class.getName() + ".isLinkOfMalwares", jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.NetLink.class.getName() + ".isLinkOfObservedData", jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.NetLink.class.getName() + ".isLinkOfReports", jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.NetLink.class.getName() + ".isLinkOfThreatActors", jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.NetLink.class.getName() + ".isLinkOfTools", jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.NetLink.class.getName() + ".isLinkOfVulnerabilities", jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.ObservedData.class.getName(), jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.Report.class.getName(), jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.ThreatActor.class.getName(), jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.ThreatActor.class.getName() + ".isTargetsThreatActorToVulnerabilities", jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.ThreatActor.class.getName() + ".isUsesThreatActorToTools", jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.Tool.class.getName(), jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.Tool.class.getName() + ".isTargetsToolToVulnerabilities", jcacheConfiguration);
            cm.createCache(com.peploleum.insight.domain.Vulnerability.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
