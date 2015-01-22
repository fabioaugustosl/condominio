package br.com.virtz.condominio.controller.beanview;

import br.com.virtz.condominio.controller.util.UtilTipoVotacao;
import br.com.virtz.condominio.entidades.Votacao;

public class VotacaoView {
	private Votacao votacao;
	private UtilTipoVotacao util;
	private boolean votou;

	
	public VotacaoView(Votacao votacao) {
		super();
		this.votacao = votacao;
		this.util = new UtilTipoVotacao();
		this.util.tratarTipoVotacaoExibicao(votacao.getTipoVotacao());
		this.votou = Boolean.FALSE;
	}
	

	public Votacao getVotacao() {
		return votacao;
	}

	public void setVotacao(Votacao votacao) {
		this.votacao = votacao;
	}

	public UtilTipoVotacao getUtil() {
		return util;
	}

	public boolean getVotou() {
		return votou;
	}

	public void setVotou(boolean votou) {
		this.votou = votou;
	}

}
