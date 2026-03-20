package com.example.demo.repository;

import com.example.demo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// @Repository indica que essa interface é um componente de acesso a dados
// JpaRepository<Usuario, Long>:
//   - Usuario = tipo da entidade
//   - Long = tipo do ID
// O Spring já implementa os métodos básicos: save(), findById(), findAll(), delete()...
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // O Spring Data JPA cria a query SQL automaticamente a partir do nome do método!
    // Equivale a: SELECT * FROM usuarios WHERE email = ?
    Optional<Usuario> findByEmail(String email);
}