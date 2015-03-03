package br.com.virtz.condominio.entidades;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "Bloco.recuperarPorCondominioComApts", 
			query = "Select distinct b FROM Bloco b "
					+ " LEFT JOIN FETCH b.apartamentos apts "
					+ " WHERE b.condominio.id = :idCondominio ") })
public class Bloco extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "numero", nullable = true)
	private Integer numero;

	@NotNull
	@Column(name = "nome", length = 50, nullable = false)
	private String nome;
	
	@ManyToOne
	@JoinColumn(name="idCondominio", nullable=false)
	private Condominio condominio;
	
	@OneToMany(mappedBy="bloco", cascade = CascadeType.ALL)
	private List<Apartamento> apartamentos;
	
	@Column(name="qtdAndares")
	private Integer quantidadeAndares;
	

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

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public Condominio getCondominio() {
		return condominio;
	}

	public void setCondominio(Condominio condominio) {
		this.condominio = condominio;
	}

	public List<Apartamento> getApartamentos() {
		return apartamentos;
	}

	public void setApartamentos(List<Apartamento> apartamentos) {
		this.apartamentos = apartamentos;
	}

	public Integer getQuantidadeAndares() {
		return quantidadeAndares;
	}

	public void setQuantidadeAndares(Integer quantidadeAndares) {
		this.quantidadeAndares = quantidadeAndares;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null){
			return false;
		}
		
		if (!(obj instanceof Entidade))
			return false;

		if (obj == this)
			return true;

		if(this.getId() != null){
			return this.getId().equals(((Bloco)obj).getId());
		}
		
		if(this.getNome() != null){
			return this.getNome().equals(((Bloco)obj).getNome());
		}
		
		if(this.getNumero() != null){
			return this.getNumero().equals(((Bloco)obj).getNumero());
		}
		
		return true;
	}
	
	
	@Override
	public int hashCode() {
	    int hash = 7;
	    if(this.getId() != null){
	    	hash = 23 * hash + (this.getId()!= null ? this.getId().hashCode() : 0);
	    } else if(this.getNome() != null){
	    	hash = 23 * hash + (this.getNome()!= null ? this.getNome().hashCode() : 0);
		} else if(this.getNumero() != null){
			hash = 23 * hash + (this.getNumero()!= null ? this.getNumero().hashCode() : 0);
		}
	    return hash;
	}
	
			
}