package apirest.prueba.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import apirest.prueba.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByEmail(String email);

}