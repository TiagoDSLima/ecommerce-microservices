package com.br.ecommerce.participante.config;

import com.br.ecommerce.participante.controller.ParticipanteController;
import com.br.ecommerce.participante.service.ParticipanteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ParticipanteController.class)
@Import(SecurityConfig.class)
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ParticipanteService participanteService;

    @Test
    void usuarioPodeBuscarOProprioParticipante() throws Exception {
        mockMvc.perform(get("/participante/usuario/5")
                        .with(jwt().jwt(j -> j.subject("5")).authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isOk());
    }

    @Test
    void usuarioNaoPodeBuscarParticipanteDeOutroUsuario() throws Exception {
        mockMvc.perform(get("/participante/usuario/5")
                        .with(jwt().jwt(j -> j.subject("6")).authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void servicoPodeBuscarParticipanteDeQualquerUsuario() throws Exception {
        mockMvc.perform(get("/participante/usuario/5")
                        .with(jwt().jwt(j -> j.subject("pedido")).authorities(new SimpleGrantedAuthority("ROLE_SERVICE"))))
                .andExpect(status().isOk());
    }

    @Test
    void listarTodosExigeAdmin() throws Exception {
        mockMvc.perform(get("/participante").with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isForbidden());
        mockMvc.perform(get("/participante").with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isOk());
    }

    @Test
    void buscarPorIdNaoPermiteUser() throws Exception {
        mockMvc.perform(get("/participante/1").with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void semTokenDeveRetornar401() throws Exception {
        mockMvc.perform(get("/participante/usuario/5"))
                .andExpect(status().isUnauthorized());
    }
}
