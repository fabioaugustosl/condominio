package br.com.virtz.boleto.bean;

import org.apache.commons.lang.StringUtils;

public class NossoNumero {

	private String numero;
	private String digito;
		
	public NossoNumero(String numero) {
		super();
		this.numero = numero;
	}
	
	public NossoNumero(String numero, String digito) {
		super();
		this.numero = numero;
		this.digito = digito;
	}
	
	
	public String getDigito() {
		return digito;
	}
	public void setDigito(String digito) {
		this.digito = digito;
	}
	public String getNumero() {
		return numero;
	}

	public String getNossoNumero(){
		if(StringUtils.isNotBlank(this.numero) && StringUtils.isBlank(this.digito)){
			return numero;
		}
		return this.numero+"-"+digito;
	}
	
	
}
