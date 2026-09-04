package apirest.prueba.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apirest.prueba.dto.AuthResponseDto;
import apirest.prueba.dto.LoginRequest;
import apirest.prueba.entities.Usuario;
import apirest.prueba.security.JwtSecurityService;
import apirest.prueba.service.UsuarioService;
import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {
	// Este controlador esta dedicado unica y exclusivamente a autenticaciones

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private JwtSecurityService jwtService;

	@Autowired
	private PasswordEncoder passwordEncoder;
	

	// Ruta para el login, al pasar la validación de usuario, se genera el token y utiliza DTO para mapear la respuesta del login y no devolver datos sensibles como contraseñas
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest body) {

		Usuario usuario = usuarioService.findByEmail(body.getEmail());

		if (usuario == null || !passwordEncoder.matches(body.getPassword(), usuario.getPassword())) {

			return ResponseEntity.status(401).body("Credenciales invalidas");
		}

		String token = jwtService.generateToken(usuario.getEmail());

		AuthResponseDto respuesta = new AuthResponseDto(
				token,
				usuario.getNombre(),
				usuario.getEmail()
		);

		return ResponseEntity.ok(respuesta);
	}
	
	// Este endpoint es para validar el token del usuario logueado
	@PostMapping("/validate")
	public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authorization) {

		String token = authorization.replace("Bearer ", "");

		if (!jwtService.validateToken(token)) {
			return ResponseEntity.status(401).body("Token inválido o caducado");
		}

		return ResponseEntity.ok("Token válido");
	}

	// Este endpoint es para obtener un token nuevo
	@PostMapping("/refresh")
	public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String authorization) {

		String token = authorization.replace("Bearer ", "");

		if (!jwtService.validateToken(token)) {
			return ResponseEntity.status(401).body("Token inválido o caducado");
		}

		String email = jwtService.getEmailFromToken(token);

		Usuario usuario = usuarioService.findByEmail(email);

		if (usuario == null) {
			return ResponseEntity.status(401).body("Usuario no encontrado");
		}

		String nuevoToken = jwtService.refreshToken(token);

		AuthResponseDto respuesta = new AuthResponseDto(
				nuevoToken,
				usuario.getNombre(),
				usuario.getEmail()
		);

		return ResponseEntity.ok(respuesta);
	}
	
	// Este endpoint inicia el flujo SSO simulado, genera un codigo de prueba y redirige al callback del frontend mediante una respuesta HTTP 302
	@GetMapping("/sso")
	public ResponseEntity<?> iniciarSso() {

		// generamos el codigo simulado
		String codigo = "codigo-sso-prueba";

		// simulamos el email del usuario autenticado por el proveedor
		String emailUsuarioAutenticado = "usuario@prueba.com";

		// construimos la url a donde enviaremos al usuario
		String urlRedireccion = "http://localhost:4200/sso/callback?code="
				+ codigo
				+ "&email="
				+ emailUsuarioAutenticado;

		return ResponseEntity
				.status(HttpStatus.FOUND)
				.location(URI.create(urlRedireccion))
				.build();
	}

	// Este endpoint procesa el callback del SSO simulado, valida el codigo recibido, obtiene el usuario y genera el token JWT.
	@GetMapping("/sso/callback")
	public ResponseEntity<?> callbackSso(
			@RequestParam String code,
			@RequestParam String email) {

		if (!"codigo-sso-prueba".equals(code)) {
			return ResponseEntity.status(401).body("Codigo SSO invalido");
		}

		Usuario usuario = usuarioService.findByEmail(email);

		if (usuario == null) {
			return ResponseEntity.status(401).body("Usuario SSO no encontrado");
		}

		String token = jwtService.generateToken(usuario.getEmail());

		AuthResponseDto respuesta = new AuthResponseDto(
				token,
				usuario.getNombre(),
				usuario.getEmail()
		);

		return ResponseEntity.ok(respuesta);
	}
	

}