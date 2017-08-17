package br.com.virtz.condominio.entidades;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity(name="areacomum")
@XmlRootElement
public class AreaComum extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(name = "nome", length = 100, nullable = false)
	private String nome;

	@Column(name = "descricao")
	private String descricao;

	@XmlTransient
	@ManyToOne
	@JoinColumn(name = "idCondominio", nullable = false)
	private Condominio condominio;

	@Column(name = "podeSerReservado")
	private boolean podeSerReservado;

	@Column(name = "taxaReserva")
	private Double taxaReserva;

	@Column(name = "instrucoesReserva", length=4800)
	private String instrucoesReserva;

	@Column(name = "dataAbertura")
	private Calendar dataAbertura;

	@Column(name = "dataFechamento")
	private Calendar dataFechamento;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Condominio getCondominio() {
		return condominio;
	}

	public void setCondominio(Condominio condominio) {
		this.condominio = condominio;
	}

	public boolean isPodeSerReservado() {
		return podeSerReservado;
	}

	public void setPodeSerReservado(boolean podeSerReservado) {
		this.podeSerReservado = podeSerReservado;
	}

	public String getInstrucoesReserva() {
		return instrucoesReserva;
	}

	public void setInstrucoesReserva(String instrucoesReserva) {
		this.instrucoesReserva = instrucoesReserva;
	}

	public Calendar getDataAbertura() {
		return dataAbertura;
	}

	public void setDataAbertura(Calendar dataAbertura) {
		this.dataAbertura = dataAbertura;
	}

	public Calendar getDataFechamento() {
		return dataFechamento;
	}

	public void setDataFechamento(Calendar dataFechamento) {
		this.dataFechamento = dataFechamento;
	}

	public Double getTaxaReserva() {
		return taxaReserva;
	}

	public void setTaxaReserva(Double taxaReserva) {
		this.taxaReserva = taxaReserva;
	}

}