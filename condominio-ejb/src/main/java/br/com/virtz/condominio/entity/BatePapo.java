package br.com.virtz.condominio.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class BatePapo extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "idUsuario", nullable = false)
	private Usuario usuario;

	@ManyToOne
	@JoinColumn(name = "idCondominio", nullable = false)
	private Condominio condominio;

	@Column(name = "mensagem", length = 800)
	private String mensagem;

	@ManyToMany
	@JoinTable(name = "AvaliacaoBatePapo", joinColumns = { @JoinColumn(name = "idBatePapo") }, inverseJoinColumns = { @JoinColumn(name = "idAvaliacao") })
	private Set<Avaliacao> avaliacoesPositivas;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Condominio getCondominio() {
		return condominio;
	}

	public void setCondominio(Condominio condominio) {
		this.condominio = condominio;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public Set<Avaliacao> getAvaliacoesPositivas() {
		return avaliacoesPositivas;
	}

	public void setAvaliacoesPositivas(Set<Avaliacao> avaliacoesPositivas) {
		this.avaliacoesPositivas = avaliacoesPositivas;
	}

}