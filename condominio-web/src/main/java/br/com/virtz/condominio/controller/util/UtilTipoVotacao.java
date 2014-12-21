package br.com.virtz.condominio.controller.util;

import br.com.virtz.condominio.constantes.EnumTipoVotacao;

public class UtilTipoVotacao {

	private boolean simNao;
	private boolean data;
	private boolean moeda;
	private boolean numerico;
	private boolean opcoes;

	
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


}
