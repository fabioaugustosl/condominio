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

@Entity
@Table(name="atalho")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "Atalho.recuperarPorCondominio",
				query = "Select n FROM Atalho n WHERE n.condominio.id = :idCondominio ORDER BY n.id DESC "),
		@NamedQuery(name = "Atalho.recuperarPorCondominioFuncionalidade",
			query = "Select n FROM Atalho n "
					+ " WHERE n.condominio.id = :idCondominio AND n.funcionalidade = :funcionalidade "
					+ " ORDER BY n.id DESC ")
})
public class Atalho extends Entidade implements Serializable {

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
	
	@Enumerated(EnumType.STRING)
	@Column(name = "funcionalidade", length=100)
	private EnumFuncionalidadeAtalho funcionalidade;


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

	public EnumFuncionalidadeAtalho getFuncionalidade() {
		return funcionalidade;
	}

	public void setFuncionalidade(EnumFuncionalidadeAtalho funcionalidade) {
		this.funcionalidade = funcionalidade;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	

}