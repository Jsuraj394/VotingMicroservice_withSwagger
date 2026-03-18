package com.Sura.VoterService.Config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI myCustomSwaggerConfig(){
        return new OpenAPI()
                .info(
                        new Info().title("Welcome to Voting Application").description("This is Voting Microservices to Cast the Votes to respected Candidates <br> Developer: <b><i> Suraj Jadhav.</i></b>")
                )
                .servers(List.of(new Server().url("http://localhost:8080/").description("Voter MS Server")))
                .tags(List.of(new Tag().name("Voter API's"), new Tag().name("") ))

                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components().addSecuritySchemes(
                        "bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")
                ))
                ;
    }
}
