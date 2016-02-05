package br.com.virtz.condominio.controller.beanview;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import br.com.virtz.condominio.controller.util.UtilTipoVotacao;
import br.com.virtz.condominio.entidades.Votacao;

public class VotacaoView {
	private Votacao votacao;
	private UtilTipoVotacao util;
	private boolean votou;
	
	private Map<String, Integer> resultadoVotacaoSelecionada;
	private Map<String, Double> resultadoPercentagemVotacaoSelecionada;

	
	public VotacaoView(Votacao votacao) {
		super();
		this.votacao = votacao;
		this.util = new UtilTipoVotacao();
		this.util.tratarTipoVotacaoExibicao(votacao.getTipoVotacao());
		this.votou = Boolean.FALSE;
		resultadoVotacaoSelecionada = new HashMap<String, Integer>();
		resultadoPercentagemVotacaoSelecionada = new HashMap<String, Double>();
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
		montarVotacaoPercentagem();
	}
	
	private void montarVotacaoPercentagem(){
		resultadoPercentagemVotacaoSelecionada = new HashMap<String, Double>();
		// descobre o total de votos da votação
		int totalVotos = 0;
		for(Integer votos : resultadoVotacaoSelecionada.values()){
			totalVotos += votos;
		}
		
		BigDecimal totalVotosBig = new BigDecimal(totalVotos);
		
		// calcula a percentagem de cada um
		for(String chave : resultadoVotacaoSelecionada.keySet()){
			BigDecimal t = new BigDecimal(resultadoVotacaoSelecionada.get(chave));
			
			BigDecimal p = t.multiply(new BigDecimal(100)).divide(totalVotosBig,2, RoundingMode.HALF_UP);// new BigDecimal(((t.*100)/totalVotos));
					
			resultadoPercentagemVotacaoSelecionada.put(chave, p.setScale(2, RoundingMode.HALF_EVEN).doubleValue());
		}
	}
	
	public Map<String, Double> getResultadoPercentagemVotacaoSelecionada() {
		return resultadoPercentagemVotacaoSelecionada;
	}

	public void setResultadoPercentagemVotacaoSelecionada(
			Map<String, Double> resultadoPercentagemVotacaoSelecionada) {
		this.resultadoPercentagemVotacaoSelecionada = resultadoPercentagemVotacaoSelecionada;
	}

}
