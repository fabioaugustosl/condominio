package br.com.virtz.condominio.entidades;

import java.io.Serializable;

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
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.virtz.condominio.constantes.EnumTipoBalanco;

@Entity
@Table(name="categoriaitembalanco")
@XmlRootElement
@NamedQueries({ 
		@NamedQuery(name = "CategoriaItemBalanco.recuperarCategoriaItemBalanco", 
				query = "Select a FROM CategoriaItemBalanco a WHERE a.condominio.id = :idCondominio" ),
		@NamedQuery(name = "CategoriaItemBalanco.recuperarCategoriaPorNomeECondominio", 
			query = "Select a FROM CategoriaItemBalanco a WHERE a.condominio.id = :idCondominio AND a.nome = :nome" ),
		@NamedQuery(name = "CategoriaItemBalanco.recuperarCategoriaPorTipo", 
			query = "Select a FROM CategoriaItemBalanco a WHERE a.condominio.id = :idCondominio AND a.tipoBalanco = :tipo " )
})
public class CategoriaItemBalanco extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idCondominio", nullable = false)
	private Condominio condominio;
	
	@Column(name = "ativa")
	private Boolean ativa;

	@Column(name = "nome", length = 100)
	private String nome;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "tipoBalanco", nullable = false)
	private EnumTipoBalanco tipoBalanco;


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

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Boolean getAtiva() {
		return ativa;
	}

	public void setAtiva(Boolean ativa) {
		this.ativa = ativa;
	}

	public EnumTipoBalanco getTipoBalanco() {
		return tipoBalanco;
	}

	public void setTipoBalanco(EnumTipoBalanco tipoBalanco) {
		this.tipoBalanco = tipoBalanco;
	}
	
	
	
}