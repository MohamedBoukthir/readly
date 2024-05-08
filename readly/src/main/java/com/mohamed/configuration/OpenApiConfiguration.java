package com.mohamed.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Mohamed",
                        email = "medboukthir@gmail.com",
                        url = "https://github.com/MohamedBoukthir"
                ),
                description = "Open API documentation for spring security",
                title = "Spring Security API",
                version = "1.0.0",
                license = @License(
                        name = "stillWorkingOnIt",
                        url = "https://stillWorkingOnIt.com" // i still working on it (fake link)
                ),
                termsOfService = "https://stillWorkingOnIt.com" // i still working on it (fake link)
        ),
        servers = {
                @Server(
                        description = "Local server",
                        url = "http://localhost:8082/api/v1"
                ),
                @Server(
                        description = "Production server",
                        url = "https://stillWorkingOnIt.com" // i still working on it (fake link)
                )
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "jwt authentication scheme",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfiguration {
}
