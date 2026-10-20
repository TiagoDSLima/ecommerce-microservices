package com.br.ecommerce.auth.service;

import com.br.ecommerce.auth.dto.ServiceTokenRequest;
import com.br.ecommerce.auth.dto.ServiceTokenResponse;
import com.br.ecommerce.auth.exception.exceptions.CredenciaisInvalidasException;
import com.br.ecommerce.auth.properties.ServiceClientsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Service
@RequiredArgsConstructor
public class ServiceTokenService {

    private final ServiceClientsProperties properties;
    private final JwtService jwtService;

    public ServiceTokenResponse gerarToken(ServiceTokenRequest request) {

        String secretEsperado = properties.serviceClients().get(request.clientId());

        if (secretEsperado == null || !segredosIguais(secretEsperado, request.clientSecret())) {
            throw new CredenciaisInvalidasException();
        }

        return new ServiceTokenResponse(
                jwtService.generateServiceToken(request.clientId()),
                jwtService.getExpiracaoServicoSegundos()
        );
    }

    private boolean segredosIguais(String esperado, String recebido) {
        return MessageDigest.isEqual(
                esperado.getBytes(StandardCharsets.UTF_8),
                recebido.getBytes(StandardCharsets.UTF_8)
        );
    }
}
