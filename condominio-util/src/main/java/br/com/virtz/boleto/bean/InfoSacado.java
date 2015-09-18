package br.com.virtz.boleto.bean;

public class InfoSacado {

	private String nome;
	private String cpf; // cpf
	private InfoEndereco endereco;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}
	
	public String getCpfFormatado() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public InfoEndereco getEndereco() {
		return endereco;
	}

	public void setEndereco(InfoEndereco endereco) {
		this.endereco = endereco;
	}

}
