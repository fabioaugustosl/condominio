package br.com.virtz.condominio.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.virtz.condominio.constantes.EnumFuncionalidadeAtalho;
import br.com.virtz.condominio.constantes.EnumTipoPublicidade;

@Entity
@Table(name="publicidade")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "Publicidade.recuperarPorCondominioTipo",
			query = "Select n FROM Publicidade n "
					+ " WHERE n.condominio.id = :idCondominio AND n.tipo = :tipo "
					+ " ORDER BY n.id DESC ")
})
public class Publicidade extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "idCondominio")
	private Condominio condominio;

	@Column(name = "texto", length=500)
	private String texto;
	
	@Column(name = "linkImagem", length=500)
	private String linkImagem;
	
	@Column(name = "link", length=500)
	private String link;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo", length=100)
	private EnumTipoPublicidade tipo;


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

	public String getLinkImagem() {
		return linkImagem;
	}

	public void setLinkImagem(String linkImagem) {
		this.linkImagem = linkImagem;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public EnumTipoPublicidade getTipo() {
		return tipo;
	}

	public void setTipo(EnumTipoPublicidade tipo) {
		this.tipo = tipo;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	
	

}