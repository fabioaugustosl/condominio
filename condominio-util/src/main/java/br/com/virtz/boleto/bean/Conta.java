package br.com.virtz.boleto.bean;

import org.apache.commons.lang.StringUtils;
import org.jrimum.bopepo.BancosSuportados;

public class Conta {

	private String numeroConta;
	private String digitoVerificadorConta;
	private Integer codigoCarteira;
	private String numeroAgencia;
	private String digitoVerificadorAgencia;
	private String codigoBanco;
	

	public BancosSuportados getBanco(){
		if(StringUtils.isNotBlank(codigoBanco)){
			for(BancosSuportados b : BancosSuportados.values()){
				if(b.getCodigoDeCompensacao().equals(codigoBanco)){
					return b;
				}
			}
		}
		return null;
	}
	
	public String getCodigoBanco() {
		return codigoBanco;
	}

	public void setCodigoBanco(String codigoBanco) {
		this.codigoBanco = codigoBanco;
	}

	public String getNumeroConta() {
		return numeroConta;
	}

	public void setNumeroConta(String numeroConta) {
		this.numeroConta = numeroConta;
	}

	public String getDigitoVerificadorConta() {
		return digitoVerificadorConta;
	}

	public void setDigitoVerificadorConta(String digitoVerificadorConta) {
		this.digitoVerificadorConta = digitoVerificadorConta;
	}

	public Integer getCodigoCarteira() {
		return codigoCarteira;
	}

	public void setCodigoCarteira(Integer codigoCarteira) {
		this.codigoCarteira = codigoCarteira;
	}

	public String getNumeroAgencia() {
		return numeroAgencia;
	}

	public void setNumeroAgencia(String numeroAgencia) {
		this.numeroAgencia = numeroAgencia;
	}

	public String getDigitoVerificadorAgencia() {
		return digitoVerificadorAgencia;
	}

	public void setDigitoVerificadorAgencia(String digitoVerificadorAgencia) {
		this.digitoVerificadorAgencia = digitoVerificadorAgencia;
	}

}
