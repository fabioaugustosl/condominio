package br.com.virtz.condominio.controller.beanview;

import java.util.HashMap;
import java.util.Map;

import br.com.virtz.condominio.controller.util.UtilTipoVotacao;
import br.com.virtz.condominio.entidades.Votacao;

public class VotacaoView {
	private Votacao votacao;
	private UtilTipoVotacao util;
	private boolean votou;
	
	private Map<String, Integer> resultadoVotacaoSelecionada;

	
	public VotacaoView(Votacao votacao) {
		super();
		this.votacao = votacao;
		this.util = new UtilTipoVotacao();
		this.util.tratarTipoVotacaoExibicao(votacao.getTipoVotacao());
		this.votou = Boolean.FALSE;
		resultadoVotacaoSelecionada = new HashMap<String, Integer>();
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

	public Map<String, Integer> getResultadoVotacaoSelecionada() {
		return resultadoVotacaoSelecionada;
	}

	public void setResultadoVotacaoSelecionada(
			Map<String, Integer> resultadoVotacaoSelecionada) {
		this.resultadoVotacaoSelecionada = resultadoVotacaoSelecionada;
	}
	

}
