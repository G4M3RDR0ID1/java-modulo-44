package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

// @Entity diz ao JPA que essa classe representa uma tabela no banco de dados
@Entity
@Table(name = "usuarios")
// @Data do Lombok gera: getters, setters, toString, equals e hashCode automaticamente
@Data
// @NoArgsConstructor gera o construtor sem argumentos (exigido pelo JPA)
@NoArgsConstructor
public class Usuario {

    // @Id marca o campo como chave primária
    @Id
    // @GeneratedValue faz o banco gerar o ID automaticamente (auto incremento)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column(unique = true) garante que não haverá dois usuários com o mesmo email
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String senha;

    // Construtor de conveniência para criar usuário com os dados necessários
    public Usuario(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }
}