package br.com.virtz.condominio.entidades;

import java.io.Serializable;

public class AcrescimoValorCobranca extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String descricao;

	private Integer mesReferencia;

	private Double valor;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getMesReferencia() {
		return mesReferencia;
	}

	public void setMesReferencia(Integer mesReferencia) {
		this.mesReferencia = mesReferencia;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Long getId() {
		return null;
	}

}