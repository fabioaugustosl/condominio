package br.com.virtz.condominio.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.virtz.condominio.constantes.EnumTipoVotacao;

@Entity
@XmlRootElement
@NamedQueries({
	@NamedQuery(name="Votacao.recuperarPorCondominio",
				query="Select v FROM Votacao v WHERE v.condominio = :idCondominio")
})
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
	
	@OneToMany(mappedBy="votacao")
	private List<OpcaoVotacao> opcoes;
	
	@OneToMany(mappedBy="votacao")
	private List<Voto> votos;
	
	
	
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

	public List<OpcaoVotacao> getOpcoes() {
		return opcoes;
	}

	public void setOpcoes(List<OpcaoVotacao> opcoes) {
		this.opcoes = opcoes;
	}

	public List<Voto> getVotos() {
		return votos;
	}

	public void setVotos(List<Voto> votos) {
		this.votos = votos;
	}
	
	
}