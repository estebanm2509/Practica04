package ec.edu.ups.modelos;

public enum RolUsuario {
	ADMINISTRADOR("Administrador"),
	EMPLEADO("Empleado");
	
	private String etiqueta;
	
	private RolUsuario(String etiqueta) {
		this.etiqueta = etiqueta;	
	}
	
	public String getEtiqueta() {
		return etiqueta;
	}
}
