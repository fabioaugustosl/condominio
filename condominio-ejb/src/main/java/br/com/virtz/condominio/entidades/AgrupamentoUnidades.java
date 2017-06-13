package br.com.virtz.condominio.entidades;

import java.io.Serializable;
import java.util.List;

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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="agrupamentounidades")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "AgrupamentoUnidades.recuperarPorCondominioComBlocos",
			query = "Select distinct b FROM AgrupamentoUnidades b "
					+ " LEFT JOIN FETCH b.blocos blocos "
					+ " WHERE b.condominio.id = :idCondominio ORDER BY b.nome "),
	@NamedQuery(name = "AgrupamentoUnidades.condominioPossuiAgrupamento",
			query = "Select count(b) FROM AgrupamentoUnidades b "
					+ " WHERE b.condominio.id = :idCondominio")
})
public class AgrupamentoUnidades extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(name = "nome", length = 50, nullable = false)
	private String nome;

	@ManyToOne
	@JoinColumn(name="idCondominio", nullable=false)
	private Condominio condominio;

	@OneToMany(mappedBy="agrupamentoUnidades")
	private List<Bloco> blocos;



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

	public Condominio getCondominio() {
		return condominio;
	}

	public void setCondominio(Condominio condominio) {
		this.condominio = condominio;
	}

	public List<Bloco> getBlocos() {
		return blocos;
	}

	public void setBlocos(List<Bloco> blocos) {
		this.blocos = blocos;
	}


}