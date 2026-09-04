package apirest.prueba.security;

import java.util.Date;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtSecurityService {

	// clave para firmar y validar los token
	private String secretKey = "adrianLoginSecretKey";

	// duracion del token 15 min
	private long expiracion = 900000;

	// genera el token usando el email del usuario
	public String generateToken(String email) {

		Date ahora = new Date();

		Date fechaExpiracion = new Date(ahora.getTime() + expiracion);

		return Jwts.builder()
				.setSubject(email)
				.setIssuedAt(ahora)
				.setExpiration(fechaExpiracion)
				.signWith(SignatureAlgorithm.HS256, secretKey)
				.compact();
	}

	// recuperamos el email guardado en el token
	public String getEmailFromToken(String token) {

		Claims datosToken = Jwts.parser()
				.setSigningKey(secretKey)
				.parseClaimsJws(token)
				.getBody();

		return datosToken.getSubject();
	}

	// valida que el token sea correcto y no este caducado
	public boolean validateToken(String token) {

		try {

			Jwts.parser()
				.setSigningKey(secretKey)
				.parseClaimsJws(token);

			return true;

		} catch (Exception e) {

			return false;
		}
	}

	// genera un token nuevo para el mismo usuario
	public String refreshToken(String token) {

		String email = getEmailFromToken(token);

		return generateToken(email);
	}

}