package br.com.virtz.condominio.entidades;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name="categoriaservicoproduto")
@XmlRootElement
public class CategoriaServicoProduto extends Entidade implements Serializable, Comparable<CategoriaServicoProduto> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "nome", length = 400)
	private String nome;

	@Column(name = "descricao", length = 10000)
	private String descricao;

	@ManyToMany(mappedBy="categorias")
	private List<Indicacao> indicacoes;
	
	@Transient
	private Long quantidade;
	
	
	public CategoriaServicoProduto() {
		super();
	}

	
	public CategoriaServicoProduto(Long id, String nome, Long quantidade) {
		super();
		this.id = id;
		this.nome = nome;
		this.quantidade = quantidade;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Long getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Long quantidade) {
		this.quantidade = quantidade;
	}

	public List<Indicacao> getIndicacoes() {
		return indicacoes;
	}

	public void setIndicacoes(List<Indicacao> indicacoes) {
		this.indicacoes = indicacoes;
	}


	@Override
	public int compareTo(CategoriaServicoProduto o) {
		if(o == null){
			return 1;
		}
		if(this.getNome() == null){
			if(o.getNome() != null){
				return -1;
			} else {
				return 0;
			}
		}
		
		return this.getNome().compareTo(o.getNome());
	}
	
}