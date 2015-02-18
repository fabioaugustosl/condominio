package br.com.virtz.condominio.entidades;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

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
	@NamedQuery(name = "Condominio.recuperarPorCidade", 
			query = "Select c FROM Condominio c WHERE c.cidade.id = :idCidade ") })
public class Condominio extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToMany(mappedBy = "condominio")
	private List<Bloco> blocos;

	@OneToMany(mappedBy = "condominio")
	private Set<AreaComum> areasComuns;

	@OneToMany(mappedBy = "condominio")
	private Set<Usuario> usuarios;

	@NotNull
	@Column(name = "nome", length = 100, nullable = false)
	private String nome;

	@Column(name = "qtdAptos")
	private Integer quantidadeApartamentos;

	@Column(name = "endereco", length = 100)
	private String endereco;

	@Column(name = "numero")
	private Integer numero;

	@Column(name = "complemento", length = 100)
	private String complemento;

	@Column(name = "bairro", length = 100)
	private String bairro;

	@Column(name = "cep", length = 20)
	private String cep;

	@Column(name = "telefone", length = 20)
	private String telefone;

	@ManyToOne
	@JoinColumn(name = "idCidade")
	private Cidade cidade;

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

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

}