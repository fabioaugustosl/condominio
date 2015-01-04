package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.entidades.BatePapo;
import br.com.virtz.condominio.entidades.ComentarioBatePapo;

@Stateless
public class ComentarioBatePapoDAO extends DAO<ComentarioBatePapo> implements IComentarioBatePapoDAO {


	@Override
	public List<ComentarioBatePapo> recuperar(BatePapo batePapo) {
		if(batePapo == null || batePapo.getId() == null){
			return null;
		}
		
		Query qry = getEntityManager().createNamedQuery("ComentarioBatePapo.recuperarPorBatePapo");
		qry.setParameter("idBatePapo", batePapo.getId());
		
		return qry.getResultList();
	}

}
