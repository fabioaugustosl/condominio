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

@Entity(name="mensagemsindico")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "MensagemSindico.recuperarPorCondominio",
				query = "Select n FROM mensagemsindico n WHERE n.condominio.id = :idCondominio ORDER BY n.id DESC ")
})
public class MensagemSindico extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Temporal(TemporalType.DATE)
	@Column(name = "data")
	private Date data;

	@ManyToOne
	@JoinColumn(name = "idCondominio")
	private Condominio condominio;

	@OneToOne
	@JoinColumn(name = "idUusario")
	private Usuario usuario;

	@Column(name = "mensagem", length=100000)
	private String mensagem;

	@OneToMany(mappedBy="mensagemSindico", fetch= FetchType.EAGER, cascade= CascadeType.ALL)
	private List<RespostaMensagemSindico> respostas;



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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<RespostaMensagemSindico> getRespostas() {
		return respostas;
	}

	public void setRespostas(List<RespostaMensagemSindico> respostas) {
		this.respostas = respostas;
	}


}