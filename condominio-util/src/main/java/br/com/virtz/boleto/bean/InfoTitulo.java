package br.com.virtz.boleto.bean;

import java.util.Date;

import org.jrimum.domkee.financeiro.banco.febraban.TipoDeTitulo;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo.Aceite;

public class InfoTitulo {

	// informações titulo
	private String numeroDocumento;
	private String nossoNumero;
	private String digitoNossoNumero;
	private Double valor;
	private Date dataDocumento;
	private Date dataVencimento;
	private String aceite = "N"; // A ou N
	private Double desconto = 0d;
	private Double deducao = 0d;
	private Double mora = 0d;
	private Double acrescimo = 0d;
	private Double valorCobrado = 0d;
	
	// informações gerais boleto
	private String descricaoLocalPagamento;
	private String instrucoesSacado;
	private String instrucaoLinha1;
	private String instrucaoLinha2;
	private String instrucaoLinha3;
	private String instrucoes;
	
	

	public Aceite getAceiteConvertido() {
		return Aceite.valueOf(aceite);
	}

	public TipoDeTitulo getTipoTitulo() {
		return TipoDeTitulo.COTA_CONDOMINIAL;
	}

	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public String getNossoNumero() {
		return nossoNumero;
	}

	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}

	public String getDigitoNossoNumero() {
		return digitoNossoNumero;
	}

	public void setDigitoNossoNumero(String digitoNossoNumero) {
		this.digitoNossoNumero = digitoNossoNumero;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Date getDataDocumento() {
		return dataDocumento;
	}

	public void setDataDocumento(Date dataDocumento) {
		this.dataDocumento = dataDocumento;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public Double getDesconto() {
		return desconto;
	}

	public void setDesconto(Double desconto) {
		this.desconto = desconto;
	}

	public Double getDeducao() {
		return deducao;
	}

	public void setDeducao(Double deducao) {
		this.deducao = deducao;
	}

	public Double getMora() {
		return mora;
	}

	public void setMora(Double mora) {
		this.mora = mora;
	}

	public Double getAcrescimo() {
		return acrescimo;
	}

	public void setAcrescimo(Double acrescimo) {
		this.acrescimo = acrescimo;
	}

	public Double getValorCobrado() {
		return valorCobrado;
	}

	public void setValorCobrado(Double valorCobrado) {
		this.valorCobrado = valorCobrado;
	}

	public String getDescricaoLocalPagamento() {
		return descricaoLocalPagamento;
	}

	public void setDescricaoLocalPagamento(String descricaoLocalPagamento) {
		this.descricaoLocalPagamento = descricaoLocalPagamento;
	}

	public String getInstrucoesSacado() {
		return instrucoesSacado;
	}

	public void setInstrucoesSacado(String instrucoesSacado) {
		this.instrucoesSacado = instrucoesSacado;
	}

	public String getInstrucoes() {
		return instrucoes;
	}

	public void setInstrucoes(String instrucoes) {
		this.instrucoes = instrucoes;
	}

	public String getInstrucaoLinha1() {
		return instrucaoLinha1;
	}

	public void setInstrucaoLinha1(String instrucaoLinha1) {
		this.instrucaoLinha1 = instrucaoLinha1;
	}

	public String getInstrucaoLinha2() {
		return instrucaoLinha2;
	}

	public void setInstrucaoLinha2(String instrucaoLinha2) {
		this.instrucaoLinha2 = instrucaoLinha2;
	}

	public String getInstrucaoLinha3() {
		return instrucaoLinha3;
	}

	public void setInstrucaoLinha3(String instrucaoLinha3) {
		this.instrucaoLinha3 = instrucaoLinha3;
	}
	
}
