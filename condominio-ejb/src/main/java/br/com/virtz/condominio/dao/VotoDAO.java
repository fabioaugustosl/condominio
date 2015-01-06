package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.entidades.Votacao;
import br.com.virtz.condominio.entidades.Voto;

@Stateless
public class VotoDAO extends DAO<Voto> implements IVotoDAO {

	
	@Override
	public Long totalVotos(Votacao votacao) {
		Query qry = getEntityManager().createNamedQuery("Voto.totalVotoPorVotacao");
		qry.setParameter("idVotacao", votacao.getId());
		return (Long) qry.getSingleResult();
	}

	@Override
	public Voto recuperarPorUsuario(Votacao votacao, Usuario usuario) {
		Query qry = getEntityManager().createNamedQuery("Voto.recuperarPorUsuario");
		qry.setParameter("idVotacao", votacao.getId());
		qry.setParameter("idUsuario", usuario.getId());
		List<Voto> votos = qry.getResultList();
		return (votos == null || votos.isEmpty()) ? null :votos.get(0);
	}
	
	

	
}
