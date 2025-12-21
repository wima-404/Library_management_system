package com.bibliotheque.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI bibliothequeOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestion de Bibliothèque")
                        .description("API REST complète pour la gestion d'une bibliothèque en ligne. " +
                                "Permet de gérer les livres, auteurs, utilisateurs, emprunts et réservations. " +
                                "Inclut le suivi des emprunts en retard et les notifications par email.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Support Bibliothèque")
                                .email("support@bibliotheque.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Serveur de développement")
                ));
    }
}


