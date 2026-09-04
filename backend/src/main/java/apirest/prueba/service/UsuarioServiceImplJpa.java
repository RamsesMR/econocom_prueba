package apirest.prueba.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import apirest.prueba.entities.Usuario;
import apirest.prueba.repository.UsuarioRepository;

@Service
public class UsuarioServiceImplJpa implements UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public Usuario findByEmail(String email) {

		return usuarioRepository.findByEmail(email);
	}

}