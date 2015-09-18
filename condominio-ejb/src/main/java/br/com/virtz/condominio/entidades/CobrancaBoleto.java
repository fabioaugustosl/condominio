package br.com.virtz.condominio.entidades;

import java.io.Serializable;
import java.util.List;

public class CobrancaBoleto extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private Condominio condominio;

	private Integer anoReferencia;

	private Integer mesReferencia;

	private Double valorBase;

	private String instrucaoLinha1;

	private String instrucaoLinha2;

	private String instrucaoLinha3;

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

	public List<AcrescimoValorCobranca> getAcrescimos() {
		return acrescimos;
	}

	public void setAcrescimos(List<AcrescimoValorCobranca> acrescimos) {
		this.acrescimos = acrescimos;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return null;
	}

}