package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.entidades.AcessoCFTV;

@Stateless
public class AcessoCFTVDAO extends DAO<AcessoCFTV> implements IAcessoCFTVDAO {

	@Override
	public AcessoCFTV recuperar(Long idCondominio) {
		Query qry = getEntityManager().createNamedQuery("AcessoCFTV.recuperarPorCondominio");
		qry.setParameter("idCondominio", idCondominio);
		List<AcessoCFTV> regitros = qry.getResultList();
		
		if(regitros != null && !regitros.isEmpty()){
			return regitros.get(0);
		}
		return null;
	}
	
	
	
}
