package br.com.virtz.condominio.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "Balanco.recuperarPorCondominio", 
						query = "Select u FROM Balanco u "
							+ " WHERE u.condominio.id = :idCondominio "),
				@NamedQuery(name = "Balanco.recuperarPorCondominioAnoMes", 
							query = "Select u FROM Balanco u "
								+ " WHERE u.condominio.id = :idCondominio AND u.ano = :ano AND u.mes = :mes ")
})
public class Balanco extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "idCondominio", nullable = false)
	private Condominio condominio;

	@Column(name = "ano", length = 4)
	private Integer ano;

	@Column(name = "mes", length = 2)
	private Integer mes;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data")
	private Date data;

	@Column(name = "descricao")
	private String descricao;
	
	
	@OneToMany(mappedBy="balanco", fetch=FetchType.EAGER, cascade = CascadeType.REMOVE)
	private List<ItemBalanco> itens;

	
	
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

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}
	
	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<ItemBalanco> getItens() {
		return itens;
	}

	public void setItens(List<ItemBalanco> itens) {
		this.itens = itens;
	}
	

}