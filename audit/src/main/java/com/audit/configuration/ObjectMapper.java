package com.audit.component;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class ObjectMapper {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
