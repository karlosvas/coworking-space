package com.grupo05.coworking_space.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración de Swagger para la documentación de la API.
 * Esta clase define la configuración de OpenAPI para la documentación
 * y establece los requisitos de seguridad para autenticación JWT.
 * @Configuration es una anotación de Spring que indica que una clase declara uno o más métodos anotados con @Bean y puede ser procesada por el contenedor de Spring para generar definiciones de beans y solicitudes de servicio para esos beans en tiempo de ejecución.
 * @OpenAPIDefinition es una anotación de Swagger que define la información general de la API, como el título, la versión y la descripción.
 * @SecurityScheme es una anotación de Swagger que define un esquema de seguridad para la API.
 */
@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Coworking Space API",
        version = "1.0",
        description = "Documentación de la API para gestionar usuarios, reservas y salas, para espacios de coworking",
        termsOfService = "http://swagger.io/terms/"
    ), security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    paramName = HttpHeaders.AUTHORIZATION,
    in = SecuritySchemeIn.HEADER,
    scheme = "bearer",
    bearerFormat = "JWT"
)

public class SwaggerConfig {
    /**
     * Esta clase no contiene métodos ya que utiliza anotaciones a nivel de clase
     * para configurar Swagger. Las anotaciones definen:
     * 
     * - El título, versión y descripción de la API
     * - El esquema de seguridad basado en JWT
     * - Los requisitos de autenticación para los endpoints
     */

     // Custom Swagger UI Configuration
    @Bean
    public WebMvcConfigurer swagger3Configurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                 // Permitir acceso a recursos estáticos
                registry.addResourceHandler("/swagger-ui/**")
                    .addResourceLocations("classpath:/META-INF/resources/webjars/springdoc-openapi-ui/")
                    .resourceChain(false);

                // Agregar ruta a recursos estáticos personalizados
                registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .resourceChain(false);
            }
        };
    }
}