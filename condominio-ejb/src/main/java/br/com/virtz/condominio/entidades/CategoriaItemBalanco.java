package br.com.virtz.condominio.entidades;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.List;

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
import javax.persistence.Transient;
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
public class CategoriaItemBalanco extends Entidade implements Serializable, Comparable<CategoriaItemBalanco> {

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
	
	@Transient
	private List<ItemBalanco> itens;

	@Transient
	private Double totalCategoria;

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

	public List<ItemBalanco> getItens() {
		return itens;
	}

	public void setItens(List<ItemBalanco> itens) {
		this.itens = itens;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof CategoriaItemBalanco)){
			return false;
		}
		CategoriaItemBalanco c = (CategoriaItemBalanco) obj;
		if(c.getId() != null){
			return super.equals(obj);
		}
		if(this.getNome() != null && this.getNome().equals(c.getNome()) &&  this.getTipoBalanco() != null && this.getTipoBalanco().equals(c.getTipoBalanco())){
			return true;
		}
		return false;
	}

	public Double getTotalCategoria() {
		return totalCategoria;
	}
	
	public String getTotalCategoriaFormatado() {
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		return formatter.format(this.totalCategoria);
	}

	public void setTotalCategoria(Double totalCategoria) {
		this.totalCategoria = totalCategoria;
	}

	@Override
	public int compareTo(CategoriaItemBalanco o) {
		if(o == null || o.getNome() == null){
			return -1;
		}
		if("OUTROS".equals(o.getNome().toUpperCase())){
			return -1;
		}
		if(this.getNome() == null && o.getNome() !=null){
			return 1;
		}
		return this.getNome().compareTo(o.getNome());
	}
	
	
}