package net.atopecode.authservice.config.swagger;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    public final static String SECURITY_SCHEMA_NAME = "bearerAuth";

    @Bean
    public OpenAPI springOpenApi() {
        OpenAPI openApi = new OpenAPI()
                .info(new Info().title("Auth Service API")
                        .description("API for management of Users and Roles")
                        .version("v1.0.0")
                        .license(new License().name("Copyright atopecode©2022").url("http://atopecode.net")));
                /*.externalDocs(new ExternalDocumentation()
                        .description("")
                        .url());*/

        //Autorización con JWT:
        openApi
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEMA_NAME, new SecurityScheme()
                                .name(SECURITY_SCHEMA_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
                //Si se descomentan las siguientes líneas, se añadiría la seguridad en la web de SwaggeUI a todos los Actions
                //de todos los Controllers del Servicio Web, en vez de usar '@SecurityRequirement(name = SECURITY_SCHEMA_NAME)'
                //en los actions que requieran autenticación:
                /*.addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName));*/

        return openApi;
    }
}
