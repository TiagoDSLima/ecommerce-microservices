package com.ecommerce.pedido.client.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Instant;

@Component
public class ServiceTokenProvider {

    private static final long MARGEM_RENOVACAO_SEGUNDOS = 30;

    private final RestClient restClient;
    private final String clientId;
    private final String clientSecret;

    private String token;
    private Instant expiraEm = Instant.EPOCH;

    public ServiceTokenProvider(RestClient.Builder restClientBuilder,
                                @Value("${ecommerce.config.clients.auth.url}") String authUrl,
                                @Value("${ecommerce.config.service-client.id}") String clientId,
                                @Value("${ecommerce.config.service-client.secret}") String clientSecret) {
        this.restClient = restClientBuilder.baseUrl(authUrl).build();
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public synchronized String getToken() {
        if (token == null || Instant.now().isAfter(expiraEm.minusSeconds(MARGEM_RENOVACAO_SEGUNDOS))) {
            ServiceTokenResponse response = restClient.post()
                    .uri("/token/servico")
                    .body(new ServiceTokenRequest(clientId, clientSecret))
                    .retrieve()
                    .body(ServiceTokenResponse.class);

            token = response.token();
            expiraEm = Instant.now().plusSeconds(response.expiresIn());
        }
        return token;
    }

    record ServiceTokenRequest(String clientId, String clientSecret) {
    }

    record ServiceTokenResponse(String token, long expiresIn) {
    }
}
