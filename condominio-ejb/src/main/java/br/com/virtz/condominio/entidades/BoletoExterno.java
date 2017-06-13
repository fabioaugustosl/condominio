package br.com.virtz.condominio.entidades;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="boletoexterno")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name="BoletoExterno.recuperarPorCondominio",
				query="Select v FROM BoletoExterno v WHERE v.condominio.id = :idCondominio ")
})
public class BoletoExterno extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "idCondominio", nullable = false)
	private Condominio condominio;

	@Column(name = "url", length = 255, nullable = false)
	private String url;
	
	@Column(name = "descricaoLink", length = 100)
	private String descricaoLink;
		
	@OneToOne(fetch= FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="idLogomarca")
	private ArquivoBoletoExterno logomarca;

	
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Condominio getCondominio() {
		return condominio;
	}

	public void setCondominio(Condominio condominio) {
		this.condominio = condominio;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescricaoLink() {
		return descricaoLink;
	}

	public void setDescricaoLink(String descricaoLink) {
		this.descricaoLink = descricaoLink;
	}

	public ArquivoBoletoExterno getLogomarca() {
		return logomarca;
	}

	public void setLogomarca(ArquivoBoletoExterno logomarca) {
		this.logomarca = logomarca;
	}

}