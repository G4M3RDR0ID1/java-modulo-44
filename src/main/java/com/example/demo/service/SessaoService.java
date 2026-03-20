package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

// @Service indica que essa classe é um componente de negócio gerenciado pelo Spring
@Service
public class SessaoService {

    // Tempo de validade da sessão: 60 segundos (1 minuto)
    private static final long DURACAO_SESSAO_SEGUNDOS = 60;

    // ConcurrentHashMap é thread-safe: ideal para armazenar sessões em memória
    // Chave: token da sessão (String UUID)
    // Valor: informações da sessão (email + horário de expiração)
    private final Map<String, DadosSessao> sessoes = new ConcurrentHashMap<>();

    /**
     * Cria uma nova sessão para o usuário e retorna o token gerado.
     */
    public String criarSessao(String email) {
        // UUID.randomUUID() gera um token único e difícil de adivinhar
        String token = UUID.randomUUID().toString();

        // Instant.now().plusSeconds() calcula o momento exato de expiração
        Instant expiracao = Instant.now().plusSeconds(DURACAO_SESSAO_SEGUNDOS);

        sessoes.put(token, new DadosSessao(email, expiracao));

        System.out.println("Sessão criada para: " + email + " | Expira em: " + expiracao);
        return token;
    }

    /**
     * Verifica se um token é válido (existe e não expirou).
     * Retorna o email do usuário se válido, ou null se inválido/expirado.
     */
    public String validarSessao(String token) {
        DadosSessao sessao = sessoes.get(token);

        // Token não existe
        if (sessao == null) {
            return null;
        }

        // Token expirou: remove da memória e retorna null
        if (Instant.now().isAfter(sessao.expiracao())) {
            sessoes.remove(token);
            System.out.println("Sessão expirada e removida para: " + sessao.email());
            return null;
        }

        return sessao.email();
    }

    /**
     * Registro interno para guardar os dados de cada sessão.
     * 'record' é um recurso moderno do Java que cria uma classe imutável automaticamente.
     */
    private record DadosSessao(String email, Instant expiracao) {}
}