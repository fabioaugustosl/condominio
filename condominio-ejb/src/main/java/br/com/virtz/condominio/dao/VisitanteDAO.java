package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.entidades.Visitante;

@Stateless
public class VisitanteDAO extends DAO<Visitante> implements IVisitanteDAO {

	@Override
	public List<Visitante> recuperar(Long idCondominio) {
		Query qry = getEntityManager().createNamedQuery("Visitante.recuperarPorCondominio");
		qry.setParameter("idCondominio", idCondominio);
		return qry.getResultList();
	}
		
}
