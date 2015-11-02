package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.entidades.Taxa;

@Stateless
public class TaxaDAO extends DAO<Taxa> implements ITaxaDAO {

	@Override
	public List<Taxa> recuperarPorCondominio(Long idCondominio) {
		Query qry = getEntityManager().createNamedQuery("Taxa.recuperarPorCondominio");
		qry.setParameter("idCondominio", idCondominio);
		return qry.getResultList();
	}

	
}
