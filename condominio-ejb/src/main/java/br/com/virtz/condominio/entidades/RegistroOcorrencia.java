package br.com.virtz.condominio.entidades;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="registrocorrencia")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "RegistroOcorrencia.recuperarPorCondominio",
				query = "Select n FROM RegistroOcorrencia n "
						+ " WHERE n.condominio.id = :idCondominio "),
		@NamedQuery(name = "RegistroOcorrencia.totalOcorrencias",
				query = "SELECT count(v) FROM RegistroOcorrencia v WHERE v.condominio.id = :idCondominio ")


})
public class RegistroOcorrencia extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dataOcorrencia")
	private Date dataOcorrencia;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dataRegistro")
	private Date dataRegistro;

	@ManyToOne
	@JoinColumn(name = "idUsuario")
	private Usuario usuario;

	@ManyToOne
	@JoinColumn(name = "idCondominio")
	private Condominio condominio;

	@Column(name = "mensagem", length=10000)
	private String mensagem;

	@OneToOne(fetch= FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="idArquivo")
	private ArquivoOcorrencia arquivo;



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataOcorrencia() {
		return dataOcorrencia;
	}

	public void setDataOcorrencia(Date dataOcorrencia) {
		this.dataOcorrencia = dataOcorrencia;
	}

	public Date getDataRegistro() {
		return dataRegistro;
	}

	public void setDataRegistro(Date dataRegistro) {
		this.dataRegistro = dataRegistro;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public ArquivoOcorrencia getArquivo() {
		return arquivo;
	}

	public void setArquivo(ArquivoOcorrencia arquivo) {
		this.arquivo = arquivo;
	}

}