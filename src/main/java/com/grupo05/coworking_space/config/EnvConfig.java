package com.grupo05.coworking_space.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.github.cdimascio.dotenv.Dotenv;

/**
 * Configuración para la carga de variables de entorno.
 * Esta clase proporciona acceso a las variables definidas en el archivo .env
 */
@Configuration
public class EnvConfig {
    
    /**
     * Crea y configura un bean Dotenv para cargar variables de entorno.
     * @Bean es una anotación que se utiliza para indicar a Spring que un método de configuración de Java debe ser tratado como un bean y devolverá un objeto que Spring debe registrar en el contexto de la aplicación.
     * @return Un objeto Dotenv configurado que ha cargado las variables del archivo .env
     */
    @Bean
    public Dotenv dotenv(){
        return Dotenv.configure().load();
    }
}