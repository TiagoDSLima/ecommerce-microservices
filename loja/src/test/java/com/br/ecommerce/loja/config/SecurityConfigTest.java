package com.br.ecommerce.loja.config;

import com.br.ecommerce.loja.controller.LojaController;
import com.br.ecommerce.loja.service.LojaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LojaController.class)
@Import(SecurityConfig.class)
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LojaService lojaService;

    @Test
    void configDaLojaDeveSerPublica() throws Exception {
        mockMvc.perform(get("/loja"))
                .andExpect(status().isOk());
    }

    @Test
    void tokenMercadoPagoSemTokenDeveRetornar401() throws Exception {
        mockMvc.perform(get("/loja/mercadopago-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void tokenMercadoPagoComUserOuAdminDeveRetornar403() throws Exception {
        mockMvc.perform(get("/loja/mercadopago-token").with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isForbidden());
        mockMvc.perform(get("/loja/mercadopago-token").with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void tokenMercadoPagoComServiceDeveRetornar200() throws Exception {
        mockMvc.perform(get("/loja/mercadopago-token").with(jwt().authorities(new SimpleGrantedAuthority("ROLE_SERVICE"))))
                .andExpect(status().isOk());
    }

    @Test
    void salvarTokenMercadoPagoExigeAdmin() throws Exception {
        String body = "{\"token\":\"abc\"}";
        mockMvc.perform(put("/loja/mercadopago-token").contentType(MediaType.APPLICATION_JSON).content(body)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isForbidden());
        mockMvc.perform(put("/loja/mercadopago-token").contentType(MediaType.APPLICATION_JSON).content(body)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isNoContent());
    }
}
