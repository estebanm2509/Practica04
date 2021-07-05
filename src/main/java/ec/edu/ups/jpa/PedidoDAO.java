package ec.edu.ups.jpa;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ec.edu.ups.modelos.Pedido;


@Stateless
public class PedidoDAO extends DAO<Pedido, Integer> {

	@PersistenceContext(unitName = "Practica04")
	private EntityManager gestor;
	
	public PedidoDAO() {
		super(Pedido.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return gestor;
	}
}
