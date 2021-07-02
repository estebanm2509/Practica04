package ec.edu.ups.jpa;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import ec.edu.ups.modelos.Cliente;

@Stateless
public class ClienteDAO extends DAO<Cliente, Integer> {

	@PersistenceContext(unitName = "Practica04")
	private EntityManager gestor;
	
	public ClienteDAO() {
		super(Cliente.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return gestor;
	}
	
	public Cliente buscarPorCedula(String cedula) {
		String jpql = "SELECT c FROM Cliente c WHERE c.cedula = '" + cedula + "'";
		Cliente cliente = null;
		try {
			cliente = gestor.createQuery(jpql, Cliente.class).getSingleResult();
		} catch (NoResultException e) {
			e.printStackTrace();
		}
		return cliente;
	}
}
