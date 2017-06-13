package br.com.virtz.boleto.bean;

import org.jrimum.domkee.comum.pessoa.endereco.UnidadeFederativa;

public class InfoEndereco {

	private String siglaEstado;
	private String cidade;
	private String cep;
	private String bairro;
	private String logradouro;
	private String numero;
	
	
	public UnidadeFederativa getUnidadeFederativa(){
		if(getSiglaEstado()!= null){
			return UnidadeFederativa.valueOfSigla(getSiglaEstado());
		}
		return null;
	}

	public String getSiglaEstado() {
		return siglaEstado;
	}

	public void setSiglaEstado(String siglaEstado) {
		this.siglaEstado = siglaEstado;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getCep() {
		return cep;
	}
	
	public String getCepFormatado() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}
	
}
