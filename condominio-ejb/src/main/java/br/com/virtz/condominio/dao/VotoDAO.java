package br.com.virtz.condominio.dao;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.entidades.Votacao;
import br.com.virtz.condominio.entidades.Voto;

@Stateless
public class VotoDAO extends DAO<Voto> implements IVotoDAO {

	
	@Override
	public Integer totalVotos(Votacao votacao) {
		Query qry = getEntityManager().createNamedQuery("Voto.totalVotoPorVotacao");
		qry.setParameter("idVotacao", votacao.getId());
		return (Integer) qry.getSingleResult();
	}


	
}
