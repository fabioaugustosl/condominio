package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.OpcaoVotacao;
import br.com.virtz.condominio.entidades.Votacao;

@Stateless
public class VotacaoDAO extends DAO<Votacao> implements IVotacaoDAO {

	
	@Override
	public List<Votacao> recuperar(Condominio condominio) {
		Query qry = getEntityManager().createNamedQuery("Votacao.recuperarPorCondominio");
		qry.setParameter("idCondominio", condominio.getId());
		return qry.getResultList();
	}

	
	@Override
	public void removerOpcaoVotacao(Long idOpcaoVotacao) {
		OpcaoVotacao opc = getEntityManager().find(OpcaoVotacao.class, idOpcaoVotacao);
		if(opc != null){
			getEntityManager().remove(opc);
		}
	}
	
}
