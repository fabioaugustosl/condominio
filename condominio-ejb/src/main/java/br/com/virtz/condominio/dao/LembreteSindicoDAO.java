package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.entidades.LembreteSindico;

@Stateless
public class LembreteSindicoDAO extends DAO<LembreteSindico> implements ILembreteSindicoDAO {

	@Override
	public List<LembreteSindico> recuperar(Long idCondominio) {
		
		if(idCondominio == null){
			return null;
		}
		
		Query qry = getEntityManager().createNamedQuery("LembreteSindico.recuperarPorCondominio");
		qry.setParameter("idCondominio", idCondominio);
		qry.setMaxResults(300);
		
		return qry.getResultList();
	}

	

}
