package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.entidades.BatePapo;
import br.com.virtz.condominio.entidades.Condominio;

@Stateless
public class BatePapoDAO extends DAO<BatePapo> implements IBatePapoDAO {

	@Override
	public List<BatePapo> recuperar(Condominio condominio) {
		
		if(condominio == null || condominio.getId() == null){
			return null;
		}
		
		Query qry = getEntityManager().createNamedQuery("BatePapo.recuperarTodosPorCondominio");
		qry.setParameter("idCondominio", condominio.getId());
		
		return qry.getResultList();
	}

}
