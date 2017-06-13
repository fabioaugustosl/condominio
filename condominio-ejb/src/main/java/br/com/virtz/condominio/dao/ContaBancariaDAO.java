package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.entidades.ContaBancariaCondominio;

@Stateless
public class ContaBancariaDAO extends DAO<ContaBancariaCondominio> implements IContaBancariaDAO {

	
	@Override
	public List<ContaBancariaCondominio> recuperarPorCondominio(Long idCondominio) {
		Query qry = getEntityManager().createNamedQuery("ContaBancariaCondominio.recuperarPorCondominio");
		qry.setParameter("idCondominio", idCondominio);
		return qry.getResultList();
	}

	
	
}
