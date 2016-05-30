package br.com.virtz.condominio.entidades;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="respostamensagemsindico")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "RespostaMensagemSindico.recuperarPorMensagem",
				query = "Select n FROM RespostaMensagemSindico n "
						+ " WHERE n.mensagemSindico.id = :idMensagem ")
})
public class RespostaMensagemSindico extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Temporal(TemporalType.DATE)
	@Column(name = "data")
	private Date data;

	@ManyToOne
	@JoinColumn(name = "idUsuario")
	private Usuario usuario;

	@ManyToOne
	@JoinColumn(name = "idMensagem")
	private MensagemSindico mensagemSindico;

	@Column(name = "mensagem", length=10000)
	private String mensagem;

	@Column(name = "paraTodos")
	private Boolean respostaParaTodos;



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public Boolean getRespostaParaTodos() {
		return respostaParaTodos;
	}

	public void setRespostaParaTodos(Boolean respostaParaTodos) {
		this.respostaParaTodos = respostaParaTodos;
	}

	public MensagemSindico getMensagemSindico() {
		return mensagemSindico;
	}

	public void setMensagemSindico(MensagemSindico mensagemSindico) {
		this.mensagemSindico = mensagemSindico;
	}


}