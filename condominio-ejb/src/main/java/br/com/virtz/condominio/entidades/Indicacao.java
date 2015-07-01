package br.com.virtz.condominio.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "Indicacao.recuperarPorCondominio", 
				query = "Select n FROM Indicacao n WHERE n.condominio.id = :idCondominio ORDER BY n.data desc ")
})
public class Indicacao extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "destaque")
	private boolean destaque;

	@Temporal(TemporalType.DATE)
	@Column(name = "data")
	private Date data;

	@ManyToOne
	@JoinColumn(name = "idCondominio")
	private Condominio condominio;
	
	@ManyToOne
	@JoinColumn(name = "idUsuario")
	private Usuario usuario;

	@Column(name = "nome", length=100)
	private String nome;
	
	@Column(name = "comentario", length=10000)
	private String comentario;
	
	@OneToMany(mappedBy="indicacao", fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<ArquivoIndicacao> arquivos;
	
	@OneToMany(mappedBy="indicacao", fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	private List<AvaliacaoIndicacao> avaliacoes;

	@ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name = "CategoriaIndicacao", 
    			joinColumns = @JoinColumn(name = "idIndicacao"), 
    			inverseJoinColumns = @JoinColumn(name = "idCategoria"))
	private Set<CategoriaServicoProduto> categorias;
	
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isDestaque() {
		return destaque;
	}

	public void setDestaque(boolean destaque) {
		this.destaque = destaque;
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public Set<ArquivoIndicacao> getArquivos() {
		return arquivos;
	}

	public void setArquivos(Set<ArquivoIndicacao> arquivos) {
		this.arquivos = arquivos;
	}

	public List<AvaliacaoIndicacao> getAvaliacoes() {
		return avaliacoes;
	}

	public void setAvaliacoes(List<AvaliacaoIndicacao> avaliacoes) {
		this.avaliacoes = avaliacoes;
	}

	public Set<CategoriaServicoProduto> getCategorias() {
		return categorias;
	}

	public void setCategorias(Set<CategoriaServicoProduto> categorias) {
		this.categorias = categorias;
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public ArquivoIndicacao getArquivoDestaque(){
		if(this.arquivos != null && !this.arquivos.isEmpty()){
			for(ArquivoIndicacao arq : this.arquivos){
				if(arq.getDestaque()){
					return arq;
				}
			}
			ArquivoIndicacao a = this.arquivos.iterator().next();
			return a;
		}
		return null;
	}
	
	
}