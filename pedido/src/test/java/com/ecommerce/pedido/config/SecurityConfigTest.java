package com.ecommerce.pedido.config;

import com.ecommerce.pedido.controller.MercadoPagoWebhookController;
import com.ecommerce.pedido.controller.PedidoController;
import com.ecommerce.pedido.dto.PedidoRequest;
import com.ecommerce.pedido.service.MercadoPagoService;
import com.ecommerce.pedido.service.PedidoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({PedidoController.class, MercadoPagoWebhookController.class})
@Import(SecurityConfig.class)
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PedidoService pedidoService;

    @MockitoBean
    private MercadoPagoService mercadoPagoService;

    @Test
    void criarPedidoSemTokenDeveRetornar401() throws Exception {
        mockMvc.perform(post("/pedido").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void criarPedidoComTokenDeServicoDeveRetornar403() throws Exception {
        mockMvc.perform(post("/pedido").contentType(MediaType.APPLICATION_JSON).content("{}")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_SERVICE"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void criarPedidoUsaOUsuarioDoToken() throws Exception {
        mockMvc.perform(post("/pedido").contentType(MediaType.APPLICATION_JSON).content("{\"idParticipante\": 999}")
                        .with(jwt().jwt(j -> j.subject("7")).authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isCreated());

        verify(pedidoService).criaPedido(any(PedidoRequest.class), eq(7L));
    }

    @Test
    void webhookDeveSerPublico() throws Exception {
        mockMvc.perform(post("/webhook/mercadopago").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isOk());
    }
}
