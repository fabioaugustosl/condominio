package br.com.virtz.condominio.dao;

import java.util.Date;
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
		List<Votacao> vot =  qry.getResultList();
		return vot;
	}

	
	@Override
	public void removerOpcaoVotacao(Long idOpcaoVotacao) {
		OpcaoVotacao opc = getEntityManager().find(OpcaoVotacao.class, idOpcaoVotacao);
		if(opc != null){
			getEntityManager().remove(opc);
		}
	}


	@Override
	public List<Votacao> recuperarVotacoesAtivas(Condominio condominio) {
		Query qry = getEntityManager().createNamedQuery("Votacao.recuperarAtivasValidasPorCondominio");
		qry.setParameter("idCondominio", condominio.getId());
		qry.setParameter("dataLimite", new Date());
		return qry.getResultList();
	}


	@Override
	public List<Votacao> recuperarVotacoesEncerradasSemEnvioDeEmail() {
		Query qry = getEntityManager().createNamedQuery("Votacao.recuperarEncerradasSemEnvioEmail");
		qry.setParameter("dataLimite", new Date());
		return qry.getResultList();
	}
	
}
