package ec.edu.ups.jpa;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ec.edu.ups.modelos.Bodega;

@Stateless
public class BodegaDAO extends DAO<Bodega, Integer> {

	@PersistenceContext(unitName = "Practica04")
	private EntityManager gestor;
	
	public BodegaDAO() {
		super(Bodega.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return gestor;
	}
	
	public Bodega buscarPorDescripcion(String descripcion) {
		String jpql = "SELECT b FROM Bodega b WHERE b.descripcion = '" + descripcion + "'";
		return gestor.createQuery(jpql, Bodega.class).getSingleResult();
	}
}
