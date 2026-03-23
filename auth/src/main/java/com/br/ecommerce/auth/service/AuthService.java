package com.br.ecommerce.auth.service;

import com.br.ecommerce.auth.dto.LoginRequest;
import com.br.ecommerce.auth.dto.LoginResponse;
import com.br.ecommerce.auth.dto.RegisterRequest;
import com.br.ecommerce.auth.enums.Role;
import com.br.ecommerce.auth.exception.exceptions.CredenciaisInvalidasException;
import com.br.ecommerce.auth.exception.exceptions.EmailJaEmUsoException;
import com.br.ecommerce.auth.model.Usuario;
import com.br.ecommerce.auth.publisher.UsuarioPublisher;
import com.br.ecommerce.auth.publisher.representation.ParticipanteRepresentation;
import com.br.ecommerce.auth.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final UsuarioPublisher usuarioPublisher;

    public LoginResponse login(LoginRequest request) {

        Usuario user = repository.findByEmail(request.email())
                .orElseThrow(() -> new CredenciaisInvalidasException());

        if (senhaIncorreta(request.senha(), user.getSenha())) {
            throw new CredenciaisInvalidasException();
        }

        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());

        return new LoginResponse(
                user.getId(),
                user.getEmail(),
                token
        );
    }

    public LoginResponse register(RegisterRequest request) {

        if (verificaSeEmailExiste(request.email())) {
            throw new EmailJaEmUsoException();
        }

        Usuario user = Usuario.builder()
                .email(request.email())
                .role(Role.ROLE_USER)
                .senha(encoder.encode(request.senha()))
                .build();

        repository.save(user);
        usuarioPublisher.publicar(new ParticipanteRepresentation(request.nome(), request.cpf(), request.dataNascimento(), user.getId()));

        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());

        return new LoginResponse(
                user.getId(),
                user.getEmail(),
                token
        );
    }

    private boolean verificaSeEmailExiste(String email) {
        return repository.findByEmail(email).isPresent();
    }

    private boolean senhaIncorreta(String senhaRequisicao, String senhaGravada) {
        return !encoder.matches(senhaRequisicao, senhaGravada);
    }
}