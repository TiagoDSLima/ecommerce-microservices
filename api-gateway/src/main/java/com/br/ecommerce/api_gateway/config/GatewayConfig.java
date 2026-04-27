package com.br.ecommerce.api_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r
                        .path("/auth/**")
                        .uri("http://localhost:8081"))
                .route("participante-service", r -> r
                        .path("/participante/**")
                        .uri("http://localhost:8082"))
                .route("produto-service", r -> r
                        .path("/produto/**")
                        .uri("http://localhost:8083"))
                .route("loja-service", r -> r
                        .path("/loja/**")
                        .uri("http://localhost:8084"))
                .route("pedido-service", r -> r
                        .path("/pedido/**")
                        .uri("http://localhost:8085"))
                .build();
    }
}