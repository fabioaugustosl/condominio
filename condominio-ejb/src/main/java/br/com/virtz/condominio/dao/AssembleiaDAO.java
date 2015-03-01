package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.entidades.Assembleia;
import br.com.virtz.condominio.entidades.OpcaoVotacao;

@Stateless
public class AssembleiaDAO extends DAO<Assembleia> implements IAssembleiaDAO {

	@Override
	public List<Assembleia> recuperar(Long idCondominio) {
		Query qry = getEntityManager().createNamedQuery("Assembleia.recuperarPorCondominio");
		qry.setParameter("idCondominio", idCondominio);
		return qry.getResultList();
	}
	
}
