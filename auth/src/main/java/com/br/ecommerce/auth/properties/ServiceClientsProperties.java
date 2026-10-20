package com.br.ecommerce.auth.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "auth")
public record ServiceClientsProperties(
        Map<String, String> serviceClients
) {
    public ServiceClientsProperties {
        serviceClients = serviceClients == null ? Map.of() : serviceClients;
    }
}
