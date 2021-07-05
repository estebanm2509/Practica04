package ec.edu.ups.rest;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ec.edu.ups.jpa.BodegaDAO;
import ec.edu.ups.jpa.FacturaDAO;
import ec.edu.ups.jpa.PedidoDAO;
import ec.edu.ups.modelos.Bodega;
import ec.edu.ups.modelos.EstadoPedido;
import ec.edu.ups.modelos.Existencia;
import ec.edu.ups.modelos.Factura;
import ec.edu.ups.modelos.FacturaDetalle;
import ec.edu.ups.modelos.Pedido;

@Path(value = "/pedidos/")
public class ServicioPedido {

	@EJB
	private BodegaDAO bodegaDAO;
	
	@EJB
	private PedidoDAO pedidoDAO;
	
	@EJB
	private FacturaDAO facturaDAO;
	
	@GET
	@Path(value = "listar")
	@Produces(value = MediaType.TEXT_PLAIN)
	public Response listarProductos(@QueryParam(value = "bodega-id") int id) {
		Jsonb constructor = JsonbBuilder.create();
		Bodega bodega = bodegaDAO.buscar(id);
		List<Existencia> existencias = bodega.getInventario()
											 .stream()
											 .sorted((e1, e2) -> e1.compareTo(e2))
											 .collect(Collectors.toList());
		return Response.ok(constructor.toJson(existencias))
				       .header("Access-Control-Allow-Origin", "*")
				       .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				       .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE")
				       .build();
	}
	
	@POST
	@Path(value = "realizar-pedido")
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Produces(value = MediaType.TEXT_PLAIN)
	public Response realizarPedido(String pedidoJSON) {
		Jsonb constructor = JsonbBuilder.create();
		try {
			Pedido pedido = constructor.fromJson(pedidoJSON, Pedido.class);
			pedidoDAO.agregar(pedido);
			Factura factura = new Factura();
			factura.setCliente(pedido.getCliente());
			pedido.getDetalles().forEach(aux -> {
				FacturaDetalle detalle = new FacturaDetalle();
				detalle.setProducto(aux.getProducto());
				detalle.setCantidad(aux.getCantidad());
				factura.getDetalles().add(detalle);
			});
			facturaDAO.agregar(factura);
			pedido.setEstado(EstadoPedido.RECEPTADO);
			pedidoDAO.modificar(pedido);
			return Response.status(200)
				   	   	   .entity("El pedido ha sido realizado.")
				   	   	   .build();
		} catch (Exception e) {
			return Response.status(404)
					   	   .entity("No se pudo realizar el pedido." + e.getMessage())
					   	   .build();
		}
	}
	
	@GET
	@Path(value = "revisar-estado-pedido")
	@Produces(value = MediaType.TEXT_PLAIN)
	public Response revisarEstadoPedido(@QueryParam(value = "id") int id) {
		Pedido pedido = pedidoDAO.buscar(id);
		if (pedido != null) {
			return Response.status(200).entity(pedido.getEstado().getEtiqueta()).build();
		} else {
			return Response.status(404).entity("No existe el pedido con el id especificado.").build();
		}
	}
}
