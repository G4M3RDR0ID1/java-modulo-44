package com.example.demo.controller;

import com.example.demo.service.SessaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AreaProtegidaController {

    private final SessaoService sessaoService;

    public AreaProtegidaController(SessaoService sessaoService) {
        this.sessaoService = sessaoService;
    }

    /**
     * GET /api/acesso
     * Rota protegida: só pode ser acessada com um token de sessão válido.
     *
     * O cliente deve enviar o header:
     * Authorization: Bearer <token>
     *
     * Fluxo:
     * 1. Extrai o token do header Authorization
     * 2. Valida o token com o SessaoService
     * 3. Se válido: retorna mensagem de acesso garantido
     * 4. Se inválido/expirado: retorna 401 com mensagem de acesso negado
     */
    @GetMapping("/acesso")
    // @RequestHeader lê um header específico da requisição HTTP
    // required = false evita erro automático se o header não vier; tratamos manualmente
    public ResponseEntity<?> acessoProtegido(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        // Verifica se o header foi enviado
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "erro", "Acesso negado.",
                            "motivo", "Você não fez login. Faça login em POST /usuarios/login para obter um token."
                    ));
        }

        // Extrai o token removendo o prefixo "Bearer "
        // Ex: "Bearer abc-123" -> "abc-123"
        String token = authHeader.substring(7);

        // Valida o token com o SessaoService
        String emailDoUsuario = sessaoService.validarSessao(token);

        if (emailDoUsuario == null) {
            // Token expirou ou não existe
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "erro", "Acesso negado.",
                            "motivo", "Sua sessão expirou ou o token é inválido. Faça login novamente."
                    ));
        }

        // Sessão válida: acesso garantido!
        return ResponseEntity.ok(Map.of(
                "mensagem", "Acesso garantido!",
                "usuario", emailDoUsuario,
                "info", "Você tem acesso a esta área protegida enquanto sua sessão for válida."
        ));
    }
}