package br.com.virtz.condominio.geral;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import br.com.virtz.condominio.constantes.EnumTipoVotacao;

public class UtilTipoVotacao {

	private boolean simNao;
	private boolean data;
	private boolean moeda;
	private boolean numerico;
	private boolean opcoes;
	private boolean opcoesImagem;
	
	private boolean valorSimNao;
	private Date valorData;
	private Double valorMoeda;
	private Double valorNumerico;
	private Long idValorOpcao;
	private Long idValorOpcaoImagem;
	
	private Map<Integer, String> opcoesSimNao = null;

	
	public UtilTipoVotacao() {
		super();
		opcoesSimNao = new HashMap<Integer, String>();
		opcoesSimNao.put(0, "Não");
		opcoesSimNao.put(1, "Sim");
		passarTodosParaFalso();
	}
	
	
	public void passarTodosParaFalso(){
		simNao = Boolean.FALSE;
		data = Boolean.FALSE;
		moeda = Boolean.FALSE;
		numerico = Boolean.FALSE;
		opcoes = Boolean.FALSE;
		opcoesImagem = Boolean.FALSE;
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
		} else if(tipoVotacao.equals(EnumTipoVotacao.OPCOES_IMAGEM)) {
			opcoesImagem = Boolean.TRUE;
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
	
	public boolean isOpcoesImagem() {
		return opcoesImagem;
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

	public Map<Integer, String> getOpcoesSimNao() {
		return opcoesSimNao;
	}

	public Long getIdValorOpcaoImagem() {
		return idValorOpcaoImagem;
	}

	public void setIdValorOpcaoImagem(Long idValorOpcaoImagem) {
		this.idValorOpcaoImagem = idValorOpcaoImagem;
	}
	

}
