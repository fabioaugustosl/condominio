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

	@Override
	public int totalVisitantes(Long idCondominio) {
		Query qry = getEntityManager().createNamedQuery("Visitante.totalVisitantes");
		qry.setParameter("idCondominio", idCondominio);
		Long total =  (Long) qry.getSingleResult();
		if(total > 1000l){
			total = 1000l;
		}
		return total.intValue();
	}

	@Override
	public List<Visitante> recuperarPaginado(Long idCondominio, int inicio, int qtdRegistros) {
		Query qry = getEntityManager().createNamedQuery("Visitante.recuperarPorCondominio");
		qry.setParameter("idCondominio", idCondominio);
		qry.setFirstResult(inicio);
		qry.setMaxResults(qtdRegistros);
		return qry.getResultList();
	}

	@Override
	public int totalVisitantesApartamento(Long idApartamento) {
		Query qry = getEntityManager().createNamedQuery("Visitante.totalVisitantesApartamento");
		qry.setParameter("idApartamento", idApartamento);
		Long total =  (Long) qry.getSingleResult();
		if(total > 1000l){
			total = 1000l;
		}
		return total.intValue();
	}

	@Override
	public List<Visitante> recuperarPaginadoApartamento(Long idApartamento, int inicio, int qtdRegistros) {
		Query qry = getEntityManager().createNamedQuery("Visitante.recuperarPorApartamento");
		qry.setParameter("idApartamento", idApartamento);
		qry.setFirstResult(inicio);
		qry.setMaxResults(qtdRegistros);
		return qry.getResultList();
	}

	
}
