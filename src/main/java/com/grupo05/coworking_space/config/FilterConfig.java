package com.grupo05.coworking_space.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.grupo05.coworking_space.filter.SwaggerUIFilter;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<SwaggerUIFilter> swaggerUiFilter() {
        FilterRegistrationBean<SwaggerUIFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new SwaggerUIFilter());
        registration.addUrlPatterns("/swagger-ui/index.html");
        return registration;
    }
}