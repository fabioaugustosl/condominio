package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.entidades.ConfiguracaoBoleto;
import br.com.virtz.condominio.entidades.ContaBancariaCondominio;

@Stateless
public class ConfiguracaoBoletoDAO extends DAO<ConfiguracaoBoleto> implements IConfiguracaoBoletoDAO {

	
	@Override
	public List<ConfiguracaoBoleto> recuperarPorCondominio(Long idCondominio) {
		Query qry = getEntityManager().createNamedQuery("ConfiguracaoBoleto.recuperarPorCondominio");
		qry.setParameter("idCondominio", idCondominio);
		return qry.getResultList();
	}
	

	@Override
	public ConfiguracaoBoleto recuperarPorCondominio(
			Long idCondominio, Integer ano, Integer mes) {
		Query qry = getEntityManager().createNamedQuery("ConfiguracaoBoleto.recuperarPorCondominioAnoMes");
		qry.setParameter("idCondominio", idCondominio);
		qry.setParameter("ano", ano);
		qry.setParameter("mes", mes);
		
		List<ConfiguracaoBoleto> resultado = qry.getResultList();
		if(resultado == null || resultado.isEmpty()){
			return null;
		}
		return resultado.get(0);
		 
	}

	
	
}
