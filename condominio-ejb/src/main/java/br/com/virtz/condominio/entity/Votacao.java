package br.com.virtz.condominio.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.virtz.condominio.constantes.EnumTipoVotacao;

@Entity
@XmlRootElement
public class Votacao extends Entidade implements Serializable {

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

	@Column(name = "assuntoVotacao", length = 2000, nullable = false)
	private String assuntoVotacao;

	@Enumerated(EnumType.STRING)
	@Column(name = "tipoVotacao", length = 20, nullable = false)
	private EnumTipoVotacao tipoVotacao;

	@Column(name = "dataLimite")
	private Date dataLimite;

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

	public String getAssuntoVotacao() {
		return assuntoVotacao;
	}

	public void setAssuntoVotacao(String assuntoVotacao) {
		this.assuntoVotacao = assuntoVotacao;
	}

	public EnumTipoVotacao getTipoVotacao() {
		return tipoVotacao;
	}

	public void setTipoVotacao(EnumTipoVotacao tipoVotacao) {
		this.tipoVotacao = tipoVotacao;
	}

	public Date getDataLimite() {
		return dataLimite;
	}

	public void setDataLimite(Date dataLimite) {
		this.dataLimite = dataLimite;
	}

}