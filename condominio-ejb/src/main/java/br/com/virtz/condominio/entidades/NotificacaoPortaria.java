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
@Table(name="notificacaoportaria")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "NotificacaoPortaria.recuperarPorCondominio",
				query = "Select n FROM NotificacaoPortaria n "
						+ " WHERE n.condominio.id = :idCondominio AND n.dataPrevista >= CURRENT_DATE AND n.dataConfirmacao is null ORDER BY n.dataPrevista desc"),
		@NamedQuery(name = "NotificacaoPortaria.recuperarNotificacoesConfirmadas",
				query = "Select n FROM NotificacaoPortaria n "
					+ " WHERE n.condominio.id = :idCondominio AND n.dataConfirmacao is not null ORDER BY n.dataConfirmacao desc")

})
public class NotificacaoPortaria extends Entidade implements Serializable {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Temporal(TemporalType.DATE)
	@Column(name = "dataPrevista")
	private Date dataPrevista;

	@ManyToOne
	@JoinColumn(name = "idUsuario")
	private Usuario usuario;

	@ManyToOne
	@JoinColumn(name = "idCondominio")
	private Condominio condominio;

	@Column(name = "nome", length=100)
	private String nome;

	@Column(name = "comentario", length=9999)
	private String comentario;

	@OneToOne(fetch= FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="idArquivo")
	private ArquivoNotificacaoPortaria arquivo;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_confirmacao")
	private Date dataConfirmacao;
	


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataPrevista() {
		return dataPrevista;
	}

	public void setDataPrevista(Date dataPrevista) {
		this.dataPrevista = dataPrevista;
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

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public ArquivoNotificacaoPortaria getArquivo() {
		return arquivo;
	}

	public void setArquivo(ArquivoNotificacaoPortaria arquivo) {
		this.arquivo = arquivo;
	}

	public Date getDataConfirmacao() {
		return dataConfirmacao;
	}

	public void setDataConfirmacao(Date dataConfirmacao) {
		this.dataConfirmacao = dataConfirmacao;
	}
	
	

}