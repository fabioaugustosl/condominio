package br.com.virtz.boleto.bean;

public class Taxa {

	private String descricao;
	private String creditoOuDebito; // C - credito | D - debito
	private String porcentagemOuValor; // P - porcentagem | V = valor
	private Double valor;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getCreditoOuDebito() {
		return creditoOuDebito;
	}

	public void setCreditoOuDebito(String creditoOuDebito) {
		this.creditoOuDebito = creditoOuDebito;
	}

	public String getPorcentagemOuValor() {
		return porcentagemOuValor;
	}

	public void setPorcentagemOuValor(String porcentagemOuValor) {
		this.porcentagemOuValor = porcentagemOuValor;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

}
