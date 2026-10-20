package com.br.ecommerce.auth.service;

import com.br.ecommerce.auth.dto.ServiceTokenRequest;
import com.br.ecommerce.auth.dto.ServiceTokenResponse;
import com.br.ecommerce.auth.exception.exceptions.CredenciaisInvalidasException;
import com.br.ecommerce.auth.properties.ServiceClientsProperties;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ServiceTokenServiceTest {

    private final ServiceTokenService service = new ServiceTokenService(
            new ServiceClientsProperties(Map.of("pedido", "segredo-pedido")),
            new JwtService("dev-secret-key-12345678901234567890")
    );

    @Test
    void deveGerarTokenQuandoCredenciaisValidas() {
        ServiceTokenResponse response = service.gerarToken(new ServiceTokenRequest("pedido", "segredo-pedido"));

        assertNotNull(response.token());
        assertEquals(300, response.expiresIn());
    }

    @Test
    void deveRejeitarSegredoInvalido() {
        assertThrows(CredenciaisInvalidasException.class,
                () -> service.gerarToken(new ServiceTokenRequest("pedido", "errado")));
    }

    @Test
    void deveRejeitarClienteDesconhecido() {
        assertThrows(CredenciaisInvalidasException.class,
                () -> service.gerarToken(new ServiceTokenRequest("loja", "segredo-pedido")));
    }
}
