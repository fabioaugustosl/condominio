package br.com.virtz.condominio.entidades;

import java.io.Serializable;
import java.util.List;

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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "Cidade.recuperarPorEstado", 
			query = "SELECT c FROM Cidade c WHERE c.estado.id = :idEstado") })
public class Cidade extends Entidade implements Serializable, Comparable<Cidade> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "estado")
	private Estado estado;

	@NotNull
	@Column(name = "uf", length = 4, nullable = false)
	private String uf;

	@NotNull
	@Column(name = "nome", length = 50, nullable = false)
	private String nome;
	
	@OneToMany(mappedBy="cidade")
	private List<Condominio> condominios;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Condominio> getCondominios() {
		return condominios;
	}

	public void setCondominios(List<Condominio> condominios) {
		this.condominios = condominios;
	}
	
	public String getNomeExibicao(){
		return "["+this.uf+"] "+this.nome;
	}

	@Override
	public int compareTo(Cidade c) {
		if(c == null){
			return 1;
		}
		
		if(this.uf != null && !this.uf.equals(c.getUf())){
			return this.uf.compareTo(c.getUf());
		} 
			
		if(this.nome != null){
			return this.nome.compareTo(c.getNome());
		}
		
		return 0;
	}

}