package com.example.demo.controller;

import com.example.demo.dto.CadastroRequest;
import com.example.demo.dto.LoginRequest;
import com.example.demo.model.Usuario;
import com.example.demo.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// @RestController = @Controller + @ResponseBody
// Indica que essa classe é um controlador REST: todos os métodos retornam JSON
@RestController
// @RequestMapping define o prefixo de URL para todos os endpoints desta classe
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * POST /usuarios/cadastro
     * Recebe os dados do usuário no corpo da requisição e salva no banco.
     *
     * Exemplo de body JSON:
     * {
     *   "nome": "João Silva",
     *   "email": "joao@email.com",
     *   "senha": "minhasenha123"
     * }
     */
    @PostMapping("/cadastro")
    // @RequestBody diz ao Spring para pegar o JSON do body e converter para CadastroRequest
    // ResponseEntity permite controlar o status HTTP da resposta
    public ResponseEntity<?> cadastrar(@RequestBody CadastroRequest request) {
        try {
            Usuario usuarioCriado = usuarioService.cadastrar(
                    request.nome(),
                    request.email(),
                    request.senha()
            );

            // Retorna 201 Created com os dados básicos do usuário criado
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Map.of(
                            "mensagem", "Usuário cadastrado com sucesso!",
                            "id", usuarioCriado.getId(),
                            "nome", usuarioCriado.getNome(),
                            "email", usuarioCriado.getEmail()
                    ));

        } catch (IllegalArgumentException e) {
            // Retorna 400 Bad Request se o email já existir
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("erro", e.getMessage()));
        }
    }

    /**
     * POST /usuarios/login
     * Valida as credenciais e retorna um token de sessão com 1 minuto de duração.
     *
     * Exemplo de body JSON:
     * {
     *   "email": "joao@email.com",
     *   "senha": "minhasenha123"
     * }
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            String token = usuarioService.login(request.email(), request.senha());

            // Retorna 200 OK com o token de sessão
            return ResponseEntity.ok(Map.of(
                    "mensagem", "Login realizado com sucesso! Sua sessão expira em 1 minuto.",
                    "token", token,
                    "instrucao", "Use o token no header: Authorization: Bearer " + token
            ));

        } catch (IllegalArgumentException e) {
            // Retorna 401 Unauthorized para credenciais inválidas
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("erro", e.getMessage()));
        }
    }
}