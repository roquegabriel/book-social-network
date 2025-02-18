package com.roque.book.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(info = @Info(contact = @Contact(name = "Roque", email = "contact@roquegabriel.com", url = "https://roquegabriel.com"), description = "OpenApi documentation for Spring Security", title = "OpenApi specification - Roque ", version = "1.0", license = @License(name = "MIT", url = "https://mit-license.org"), termsOfService = "Terms of service"), servers = {
		@Server(description = "LOCAL ENV", url = "http://localhost:8088/api/v1"),
		@Server(description = "PROD ENV", url = "https://demo.roquegabriel.com") }, security = {
				@SecurityRequirement(name = "bearerAuth") })
@SecurityScheme(name = "bearerAuth", description = "JWT auth description", scheme = "bearer", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", in = SecuritySchemeIn.HEADER)
public class OpenApiConfig {

}
