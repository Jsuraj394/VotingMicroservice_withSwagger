package com.Sura.CandidateService.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
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
                .servers(List.of(new Server().url("http://localhost:8080/").description("Candidate MS Server")))
                .tags(List.of(new Tag().name("Candidate API's"), new Tag().name("") ))

                ;

    }
}
