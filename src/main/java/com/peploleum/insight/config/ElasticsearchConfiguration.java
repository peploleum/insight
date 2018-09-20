package com.peploleum.insight.config;

import java.io.IOException;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.elasticsearch.client.Client;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableConfigurationProperties(ElasticsearchProperties.class)
@ConditionalOnProperty("spring.data.elasticsearch.cluster-nodes")
public class ElasticsearchConfiguration {

    @Bean
    public ElasticsearchTemplate elasticsearchTemplate(Client client, Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder) {
        ObjectMapper objectMapper = jackson2ObjectMapperBuilder.createXmlMapper(false).build();
        objectMapper.registerModule(new JavaTimeModule());
        return new ElasticsearchTemplate(client, new CustomEntityMapper(objectMapper));
    }

    public class CustomEntityMapper implements EntityMapper {

        private ObjectMapper objectMapper;

        public CustomEntityMapper(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
            objectMapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
            objectMapper.configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
        }

        @Override
        public String mapToString(Object object) throws IOException {
            return objectMapper.writeValueAsString(object);
        }

        @Override
        public <T> T mapToObject(String source, Class<T> clazz) throws IOException {
            return objectMapper.readValue(source, clazz);
        }
    }
}
