package com.grupo05.coworking_space.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.grupo05.coworking_space.filter.SwaggerUIFilter;

/**
 * Configuración de filtros para la aplicación.
 * Esta clase establece los filtros HTTP que se aplicarán a determinadas rutas de la aplicación.
 * 
 * La anotación @Configuration indica que esta clase es una fuente de definiciones de beans
 * y puede ser procesada por el contenedor de Spring para generar beans de componentes
 * del sistema en tiempo de ejecución.
 */
@Configuration
public class FilterConfig {

    /**
     * Configura y registra un filtro para la interfaz Swagger UI.
     * Este filtro se aplicará específicamente a la página principal de Swagger UI
     * y permitirá realizar operaciones personalizadas antes o después de acceder a esta ruta.
     * 
     * @return Un objeto FilterRegistrationBean configurado con SwaggerUIFilter
     *         que se aplicará a la ruta "/swagger-ui/index.html"
     */
    @Bean
    public FilterRegistrationBean<SwaggerUIFilter> swaggerUiFilter() {
        FilterRegistrationBean<SwaggerUIFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new SwaggerUIFilter());
        registration.addUrlPatterns("/swagger-ui/index.html");
        return registration;
    }
}