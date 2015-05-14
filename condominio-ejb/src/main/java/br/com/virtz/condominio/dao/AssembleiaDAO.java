package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import br.com.virtz.condominio.entidades.Assembleia;

@Stateless
public class AssembleiaDAO extends DAO<Assembleia> implements IAssembleiaDAO {

	@Override
	public List<Assembleia> recuperar(Long idCondominio) {
		Query qry = getEntityManager().createNamedQuery("Assembleia.recuperarPorCondominio");
		qry.setParameter("idCondominio", idCondominio);
		return qry.getResultList();
	}
	
	
	@Override
	public List<Assembleia> recuperarNaoRealizadas(Long idCondominio) {
		Query qry = getEntityManager().createNamedQuery("Assembleia.recuperarNaoRealizadasPorCondominio");
		qry.setParameter("idCondominio", idCondominio);
		return qry.getResultList();
	}

	
	@Override
	public Assembleia recuperarUltimaAssembleia(Long idCondominio) {
		Query qry = getEntityManager().createNamedQuery("Assembleia.recuperarIdUltimaAssembleiaDoCondominio");
		qry.setParameter("idCondominio", idCondominio);
		try{
			Long id = (Long) qry.getSingleResult();
			return recuperarPorId(id);
		}catch(NoResultException no){
			return null;
		}catch(NonUniqueResultException non){
			return null;
		}
	}
	
	
	
	
}
