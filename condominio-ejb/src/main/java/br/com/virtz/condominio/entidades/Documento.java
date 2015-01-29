package br.com.virtz.condominio.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "Documento.recuperarPorCondominio", 
				query = "Select n FROM Documento n WHERE n.condominio.id = :idCondominio AND n.ativo = 1 ") })
public class Documento extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "ativo")
	private boolean ativo;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "data")
	private Date data;

	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name = "idCondominio")
	private Condominio condominio;
	
	@Column(name = "titulo", length=400)
	private String titulo;
	
	@Column(name = "descricao", length=2000)
	private String descricao;
	
	@OneToOne(fetch= FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="idArquivo")
	private ArquivoDocumento arquivo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Condominio getCondominio() {
		return condominio;
	}

	public void setCondominio(Condominio condominio) {
		this.condominio = condominio;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public ArquivoDocumento getArquivo() {
		return arquivo;
	}

	public void setArquivo(ArquivoDocumento arquivo) {
		this.arquivo = arquivo;
	}

	
}