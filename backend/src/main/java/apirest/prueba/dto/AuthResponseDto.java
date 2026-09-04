package apirest.prueba.dto;

public class AuthResponseDto {

	private String token;
	private String nombre;
	private String email;

	public AuthResponseDto() {
	}

	public AuthResponseDto(String token, String nombre, String email) {
		this.token = token;
		this.nombre = nombre;
		this.email = email;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}