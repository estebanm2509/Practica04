package ec.edu.ups.modelos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "usuarios")
@DiscriminatorValue(value = "0")
public class Usuario extends Persona implements Serializable {

	private static final long serialVersionUID = -6426398269335141097L;

	@Column(name = "correo", nullable = false)
	private String correo;
	
	@Column(name = "contrasenia", nullable = false)
	private String contrasenia;
	
	@Enumerated(value = EnumType.ORDINAL)
	@Column(name = "rol", nullable = false)
	private RolUsuario rol;
	
	@Column(name = "activo", nullable = false)
	private boolean activo;
	
	public Usuario() {
		activo = true;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getContrasenia() {
		return contrasenia;
	}

	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}

	public RolUsuario getRol() {
		return rol;
	}
	
	public void setRol(RolUsuario rol) {
		this.rol = rol;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}
}
