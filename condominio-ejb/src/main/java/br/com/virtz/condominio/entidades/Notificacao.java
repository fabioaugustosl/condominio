package br.com.virtz.condominio.entidades;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.StringUtils;

import br.com.virtz.condominio.constantes.EnumTipoNotificacao;

@Entity
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "Notificacao.recuperarPorCondominioEUsuario", 
						query = "SELECT n FROM Notificacao n "
								+ " WHERE n.condominio.id = :idCondominio AND n.usuario.id = :idUsuario "
								+ " ORDER BY n.data DESC") })
public class Notificacao extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "idUsuario", nullable = false)
	private Usuario usuario;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "idCondominio", nullable = false)
	private Condominio condominio;

	@Column(name = "texto", length = 2000, nullable = false)
	private String texto;

	@Enumerated(EnumType.STRING)
	@Column(name = "tipoNotificacao", length = 20, nullable = false)
	private EnumTipoNotificacao tipoNotificacao;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data")
	private Date data;

	
	
	
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

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public EnumTipoNotificacao getTipoNotificacao() {
		return tipoNotificacao;
	}

	public void setTipoNotificacao(EnumTipoNotificacao tipoNotificacao) {
		this.tipoNotificacao = tipoNotificacao;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}
	
	public String getTextoExibicao() {
		if(EnumTipoNotificacao.AVISO.equals(this.tipoNotificacao)){
			return texto;
		}
		
		if(StringUtils.isNotBlank(this.texto) && !"-".equals(this.texto)){
			return this.texto;
		}
		
		SimpleDateFormat dt1 = new SimpleDateFormat("dd/mm/yyyy");
		StringBuilder sb = new StringBuilder();
		sb.append("Você recebeu um(a) ");
		sb.append(this.tipoNotificacao.getDescricao());
		sb.append(" dia ").append(dt1.format(this.data));
		sb.append(".");
				
		return sb.toString();
	}

}