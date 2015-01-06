package br.com.virtz.condominio.controller.beanview;

import br.com.virtz.condominio.controller.util.UtilTipoVotacao;
import br.com.virtz.condominio.entidades.Votacao;

public class VotacaoView {
	private Votacao votacao;
	private UtilTipoVotacao util;

	
	public VotacaoView(Votacao votacao) {
		super();
		this.votacao = votacao;
		this.util = new UtilTipoVotacao();
		this.util.tratarTipoVotacaoExibicao(votacao.getTipoVotacao());
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


}
