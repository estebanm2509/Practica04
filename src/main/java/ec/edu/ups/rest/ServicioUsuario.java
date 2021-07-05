package ec.edu.ups.rest;

import javax.ejb.EJB;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ec.edu.ups.jpa.ClienteDAO;
import ec.edu.ups.jpa.UsuarioDAO;
import ec.edu.ups.modelos.Cliente;
import ec.edu.ups.modelos.RolUsuario;
import ec.edu.ups.modelos.Usuario;

@Path(value = "/usuarios/")
public class ServicioUsuario {

	@EJB
	private ClienteDAO clienteDAO;
	
	@EJB
	private UsuarioDAO usuarioDAO;
	
	@GET
	@Path(value = "/iniciar-sesion")
	@Produces(value = MediaType.TEXT_PLAIN)
	public Response iniciarSesion(
			@QueryParam(value = "correo") String correo, 
			@QueryParam(value = "contrasenia") String contrasenia) {
		try {
			Usuario usuario = usuarioDAO.buscarPorCorreo(correo);
			if (usuario.getContrasenia().equals(contrasenia)) {
				return Response.ok(true).build();
			} else {
				return Response.ok(false).build();
			}
		} catch (NullPointerException e) {
			return Response.status(404)
						   .entity("El usuario con el correo especificado no existe.")
						   .build();
		}
	}
	
	@POST
	@Path(value = "/crear-cuenta")
	@Consumes(value = MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(value = MediaType.TEXT_PLAIN)
	public Response crearCuenta(
			@FormParam(value = "cedula") String cedula,
			@FormParam(value = "correo") String correo,
			@FormParam(value = "contrasenia") String contrasenia) {
		try {
			Cliente cliente = clienteDAO.buscarPorCedula(cedula);
			Usuario usuario = new Usuario();
			usuario.setDuenio(cliente);
			usuario.setCorreo(correo);
			usuario.setContrasenia(contrasenia);
			usuario.setRol(RolUsuario.CLIENTE);
			usuarioDAO.agregar(usuario);
			return Response.status(201)
						   .entity("Usuario creado correctamente.")
						   .build();	
		} catch (NullPointerException e) {
			return Response.status(404)
						   .entity("El cliente no se encuentra registrado.")
						   .build();
		}
	}
	
	@DELETE
	@Path(value = "/eliminar-cuenta")
	@Produces(value = MediaType.TEXT_PLAIN)
	public Response eliminarCuenta(@QueryParam(value = "correo") String correo) {
		try {
			Usuario usuario = usuarioDAO.buscarPorCorreo(correo);
			usuario.setActivo(false);
			usuarioDAO.modificar(usuario);
			return Response.status(204)
						   .entity("El usuario ha sido eliminado.")
						   .build();
		} catch (NullPointerException e) {
			return Response.status(404)
						   .entity("El usuario con el correo especificado no existe.")
						   .build();
		}
	}
}
