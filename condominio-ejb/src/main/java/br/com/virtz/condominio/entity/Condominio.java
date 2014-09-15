package br.com.virtz.condominio.entity;

import java.io.Serializable;
import java.util.List;

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
	private List<Bloco> bloco;
	
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

	public List<Bloco> getBloco() {
		return bloco;
	}

	public void setBloco(List<Bloco> bloco) {
		this.bloco = bloco;
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
	
	
	
}