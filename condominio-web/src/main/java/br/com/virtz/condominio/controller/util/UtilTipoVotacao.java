package br.com.virtz.condominio.controller.util;

import java.util.Date;

import br.com.virtz.condominio.constantes.EnumTipoVotacao;

public class UtilTipoVotacao {

	private boolean simNao;
	private boolean data;
	private boolean moeda;
	private boolean numerico;
	private boolean opcoes;
	
	private boolean valorSimNao;
	private Date valorData;
	private Double valorMoeda;
	private Double valorNumerico;
	private Long idValorOpcao;

	
	public UtilTipoVotacao() {
		super();
		passarTodosParaFalso();
	}
	
	
	public void passarTodosParaFalso(){
		simNao = Boolean.FALSE;
		data = Boolean.FALSE;
		moeda = Boolean.FALSE;
		numerico = Boolean.FALSE;
		opcoes = Boolean.FALSE;
	}
	
	public void tratarTipoVotacaoExibicao(EnumTipoVotacao tipoVotacao){
		passarTodosParaFalso();
		if(tipoVotacao.equals(EnumTipoVotacao.SIM_NAO)){
			simNao = Boolean.TRUE;
		} else if(tipoVotacao.equals(EnumTipoVotacao.DATA)) {
			data = Boolean.TRUE;
		} else if(tipoVotacao.equals(EnumTipoVotacao.MOEDA)) {
			moeda = Boolean.TRUE;
		} else if(tipoVotacao.equals(EnumTipoVotacao.NUMERICA)) {
			numerico = Boolean.TRUE;
		} else if(tipoVotacao.equals(EnumTipoVotacao.OPCOES)) {
			opcoes = Boolean.TRUE;
		}
	}
	

	public boolean isSimNao() {
		return simNao;
	}

	public boolean isData() {
		return data;
	}

	public boolean isMoeda() {
		return moeda;
	}

	public boolean isNumerico() {
		return numerico;
	}

	public boolean isOpcoes() {
		return opcoes;
	}

	public boolean isValorSimNao() {
		return valorSimNao;
	}

	public void setValorSimNao(boolean valorSimNao) {
		this.valorSimNao = valorSimNao;
	}

	public Date getValorData() {
		return valorData;
	}

	public void setValorData(Date valorData) {
		this.valorData = valorData;
	}

	public Double getValorMoeda() {
		return valorMoeda;
	}

	public void setValorMoeda(Double valorMoeda) {
		this.valorMoeda = valorMoeda;
	}

	public Double getValorNumerico() {
		return valorNumerico;
	}

	public void setValorNumerico(Double valorNumerico) {
		this.valorNumerico = valorNumerico;
	}

	public Long getIdValorOpcao() {
		return idValorOpcao;
	}

	public void setIdValorOpcao(Long idValorOpcao) {
		this.idValorOpcao = idValorOpcao;
	}
	
	


}
