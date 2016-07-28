package ru.croacker.test;

import com.google.gson.Gson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 *
 */
@Configuration
@ComponentScan(basePackages = {"ru.croacker.test.http", "ru.croacker.test.service"})
@PropertySource("classpath:/application.properties")
public class ContextConfiguration {

    @Bean
    public AsposeTestApplication asposeTestEntrypoint(){
        return new AsposeTestApplication();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public Gson gson(){
        return new Gson();
    }
}
