package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.entidades.Recebido;

@Stateless
public class RecebidoDAO extends DAO<Recebido> implements IRecebidoDAO {

	@Override
	public List<Recebido> recuperarPorApartamento(Long idApartamento) {
		Query qry = getEntityManager().createNamedQuery("Recebido.recuperarPorApto");
		qry.setParameter("idApartamento", idApartamento);
		return qry.getResultList();
	}

	@Override
	public List<Recebido> recuperarPorCondominio(Long idCondominio) {
		Query qry = getEntityManager().createNamedQuery("Recebido.recuperarPorCondominio");
		qry.setParameter("idCondominio", idCondominio);
		return qry.getResultList();
	}
	
	@Override
	public List<Recebido> recuperarPaginado(Long idCondominio, int inicio, int qtdRegistros) {
		Query qry = getEntityManager().createNamedQuery("Recebido.recuperarPorCondominio");
		qry.setParameter("idCondominio", idCondominio);
		qry.setFirstResult(inicio);
		qry.setMaxResults(qtdRegistros);
		return qry.getResultList();
	}
	
	@Override
	public List<Recebido> recuperarPaginadoApto(Long idApartamento, int inicio, int qtdRegistros) {
		Query qry = getEntityManager().createNamedQuery("Recebido.recuperarPorApto");
		qry.setParameter("idApartamento", idApartamento);
		qry.setFirstResult(inicio);
		qry.setMaxResults(qtdRegistros);
		return qry.getResultList();
	}
	
	@Override
	public int totalRecebidos(Long idCondominio) {
		Query qry = getEntityManager().createNamedQuery("Recebido.totalRecebidos");
		qry.setParameter("idCondominio", idCondominio);
		Long total =  (Long) qry.getSingleResult();
		if(total > 1000l){
			total = 1000l;
		}
		return total.intValue();
	}
	
	@Override
	public int totalRecebidosApto(Long idApartamento) {
		Query qry = getEntityManager().createNamedQuery("Recebido.totalRecebidosApto");
		qry.setParameter("idApartamento", idApartamento);
		Long total =  (Long) qry.getSingleResult();
		if(total > 1000l){
			total = 1000l;
		}
		return total.intValue();
	}
}
