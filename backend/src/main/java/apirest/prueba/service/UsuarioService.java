package apirest.prueba.service;

import apirest.prueba.entities.Usuario;

public interface UsuarioService {

	Usuario findByEmail(String email);

}