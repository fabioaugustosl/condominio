package br.com.virtz.condominio.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class CobrancaBoleto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Condominio condominio;

	private Integer anoReferencia;

	private Integer mesReferencia;

	private Double valorBase;

	private String instrucaoSacado;

	private String instrucaoLocal;

	private String instrucoes;
	
	private String nossoNumero;
	
	private String numeroBarras;
	
	private Date dataVencimento;

	private List<AcrescimoValorCobranca> acrescimos;

	public Condominio getCondominio() {
		return condominio;
	}

	public void setCondominio(Condominio condominio) {
		this.condominio = condominio;
	}

	public Integer getAnoReferencia() {
		return anoReferencia;
	}

	public void setAnoReferencia(Integer anoReferencia) {
		this.anoReferencia = anoReferencia;
	}

	public Integer getMesReferencia() {
		return mesReferencia;
	}

	public void setMesReferencia(Integer mesReferencia) {
		this.mesReferencia = mesReferencia;
	}

	public Double getValorBase() {
		return valorBase;
	}

	public void setValorBase(Double valorBase) {
		this.valorBase = valorBase;
	}

	public String getInstrucaoSacado() {
		return instrucaoSacado;
	}

	public void setInstrucaoSacado(String instrucaoSacado) {
		this.instrucaoSacado = instrucaoSacado;
	}

	public String getInstrucaoLocal() {
		return instrucaoLocal;
	}

	public void setInstrucaoLocal(String instrucaoLocal) {
		this.instrucaoLocal = instrucaoLocal;
	}

	public String getInstrucoes() {
		return instrucoes;
	}

	public void setInstrucoes(String instrucoes) {
		this.instrucoes = instrucoes;
	}

	public String getNossoNumero() {
		return nossoNumero;
	}

	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}

	public String getNumeroBarras() {
		return numeroBarras;
	}

	public void setNumeroBarras(String numeroBarras) {
		this.numeroBarras = numeroBarras;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public List<AcrescimoValorCobranca> getAcrescimos() {
		return acrescimos;
	}

	public void setAcrescimos(List<AcrescimoValorCobranca> acrescimos) {
		this.acrescimos = acrescimos;
	}

	
	
	

}