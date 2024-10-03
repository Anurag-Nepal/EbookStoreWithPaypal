package com.anuragnepal.itbooksnepal.Paypal;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class PaypalConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(); // Create and return a new RestTemplate instance
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper(); // Create and return a new ObjectMapper instance
    }
}