package br.com.virtz.condominio.dao;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.boleto.util.DataUtil;
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
		DataUtil util = new DataUtil();
		qry.setParameter("dataLimite", util.limparHora(new Date()));
		return qry.getResultList();
	}


	/**
	 * Recupera votações novas que estão ativas a mais de 1 dia q ainda não foi enviada aviso aos moradores
	 * @return
	 */
	@Override
	public List<Votacao> recuperarVotacoesNovasSemEnvioDeEmail() {
		Query qry = getEntityManager().createNamedQuery("Votacao.recuperarNovasSemEnvioEmail");
		DataUtil util = new DataUtil();
		//Date dataDiaAnterior = util.adicionarDias(new Date(), -1);
		qry.setParameter("dataHoje", util.limparHora(new Date()));
		return qry.getResultList();
	}


}
