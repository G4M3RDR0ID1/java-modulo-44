package com.example.demo.service;

import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    // O Spring injeta automaticamente as dependências pelo construtor (Injeção de Dependência)
    private final UsuarioRepository usuarioRepository;
    private final SessaoService sessaoService;

    // Construtor: o Spring percebe que precisa de um UsuarioRepository e um SessaoService
    // e os injeta automaticamente. Isso é o conceito de Inversão de Controle (IoC)!
    public UsuarioService(UsuarioRepository usuarioRepository, SessaoService sessaoService) {
        this.usuarioRepository = usuarioRepository;
        this.sessaoService = sessaoService;
    }

    /**
     * Cadastra um novo usuário no banco de dados.
     * Lança exceção se o email já estiver em uso.
     */
    public Usuario cadastrar(String nome, String email, String senha) {
        // Verifica se já existe um usuário com esse email
        if (usuarioRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado: " + email);
        }

        // ATENÇÃO: Em produção, NUNCA salve a senha em texto puro!
        // Use BCryptPasswordEncoder do Spring Security para fazer o hash da senha.
        // Estamos simplificando aqui apenas para fins educacionais.
        Usuario novoUsuario = new Usuario(nome, email, senha);
        return usuarioRepository.save(novoUsuario);
        // .save() executa um INSERT no banco de dados e retorna o objeto salvo (com ID preenchido)
    }

    /**
     * Realiza o login do usuário.
     * Retorna o token da sessão se as credenciais forem válidas.
     * Lança exceção se o email não existir ou a senha estiver errada.
     */
    public String login(String email, String senha) {
        // Busca o usuário pelo email; lança exceção se não encontrar
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }

        Usuario usuario = usuarioOpt.get();

        // Verifica se a senha bate
        if (!usuario.getSenha().equals(senha)) {
            throw new IllegalArgumentException("Senha incorreta.");
        }

        // Credenciais válidas: cria e retorna o token de sessão
        return sessaoService.criarSessao(email);
    }
}