package br.com.virtz.condominio.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name="taxaextramensal")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "TaxaExtraMensal.recuperarPorCondominio", 
							query = "Select u FROM taxaextramensal u "
										+ " WHERE u.condominio.id = :idCondominio ") })
public class TaxaExtraMensal extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "mes", length = 2)
	private Integer mes;

	@Column(name = "ano", length = 4)
	private Integer ano;

	@Column(name = "valor")
	private Double valor;

	@Column(name = "descricao", length = 500)
	private String descricao;

	@ManyToOne
	@JoinColumn(name = "idCondominio", nullable = false)
	private Condominio condominio;

	@ManyToOne
	@JoinColumn(name = "idConfiguracaoBoleto", nullable = false)
	private ConfiguracaoBoleto configuracaoBoleto;
	
	
	
	public Long getId() {
		return id;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
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

	public void setId(Long id) {
		this.id = id;
	}

	public ConfiguracaoBoleto getConfiguracaoBoleto() {
		return configuracaoBoleto;
	}

	public void setConfiguracaoBoleto(ConfiguracaoBoleto configuracaoBoleto) {
		this.configuracaoBoleto = configuracaoBoleto;
	}
	
	

}