package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.entidades.Balanco;

@Stateless
public class BalancoDAO extends DAO<Balanco> implements IBalancoDAO {

	
	@Override
	public List<Balanco> recuperarPorCondominio(Long idCondominio) {
		Query qry = getEntityManager().createNamedQuery("Balanco.recuperarPorCondominio");
		qry.setParameter("idCondominio", idCondominio);
		return qry.getResultList();
	}
	

	@Override
	public Balanco recuperarPorCondominio(
			Long idCondominio, Integer ano, Integer mes) {
		Query qry = getEntityManager().createNamedQuery("Balanco.recuperarPorCondominioAnoMes");
		qry.setParameter("idCondominio", idCondominio);
		qry.setParameter("ano", ano);
		qry.setParameter("mes", mes);
		
		List<Balanco> resultado = qry.getResultList();
		if(resultado == null || resultado.isEmpty()){
			return null;
		}
		return resultado.get(0);
		 
	}

	
	@Override
	public List<Balanco> recuperarPorCondominioComSomatorio(Long idCondominio) {
		Query qry = getEntityManager().createNamedQuery("Balanco.recuperarPorCondominioESomatorio");
		qry.setParameter("idCondominio", idCondominio);
		
		List<Balanco> resultado = qry.getResultList();
		return resultado;
		 
	}
	
	
}
