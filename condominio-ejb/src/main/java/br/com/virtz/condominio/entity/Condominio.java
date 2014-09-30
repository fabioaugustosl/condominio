package br.com.virtz.condominio.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class Condominio extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@OneToMany(mappedBy="condominio")
	private List<Bloco> blocos;
	
	@OneToMany(mappedBy="condominio")
	private Set<AreaComum> areasComuns;
	
	@OneToMany(mappedBy="condominio")
	private Set<Usuario> usuarios;
	
	@NotNull
	@Column(name = "nome", length = 100, nullable = false)
	private String nome;
	
	@Column(name="qtdAptos")
	private Integer quantidadeApartamentos;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Bloco> getBlocos() {
		return blocos;
	}

	public void setBlocos(List<Bloco> blocos) {
		this.blocos = blocos;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getQuantidadeApartamentos() {
		return quantidadeApartamentos;
	}

	public void setQuantidadeApartamentos(Integer quantidadeApartamentos) {
		this.quantidadeApartamentos = quantidadeApartamentos;
	}

	public Set<AreaComum> getAreasComuns() {
		return areasComuns;
	}

	public void setAreasComuns(Set<AreaComum> areasComuns) {
		this.areasComuns = areasComuns;
	}

	public Set<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(Set<Usuario> usuarios) {
		this.usuarios = usuarios;
	}
	
}