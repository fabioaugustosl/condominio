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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name="balanco")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "Balanco.recuperarPorCondominio", 
						query = "Select u FROM balanco u "
							+ " WHERE u.condominio.id = :idCondominio ORDER BY u.ano DESC, u.mes DESC"),
				@NamedQuery(name = "Balanco.recuperarPorCondominioAnoMes", 
						query = "Select u FROM balanco u "
							+ " WHERE u.condominio.id = :idCondominio AND u.ano = :ano AND u.mes = :mes "),
				@NamedQuery(name = "Balanco.recuperarPorCondominioESomatorio", 
						query = "Select new balanco (b.id, b.ano, b.mes,"
								+ "	(select sum(i.valor) FROM itembalanco i WHERE i.balanco.id = b.id AND i.tipoBalanco = 'RECEITA'),"
								+ " (select sum(i.valor) FROM itembalanco i WHERE i.balanco.id = b.id AND i.tipoBalanco = 'DESPESA')) "
							+ " FROM balanco b "
							+ " WHERE b.condominio.id = :idCondominio  ORDER BY b.ano DESC, b.mes DESC")
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

	@Transient
	private Double totalReceitas = null;
	
	@Transient
	private Double totalDespesas = null;
	
	
		
	
	public Balanco() {
		super();
	}

	
	/**
	 * Construtor utilizado na namedquery:
	 * 
	 * Balanco.recuperarPorCondominioESomatorio
	 *  
	 * @param id
	 * @param ano
	 * @param mes
	 * @param totalReceitas
	 * @param totalDespesas
	 */
	public Balanco(Long id, Integer ano, Integer mes, Double totalReceitas,
			Double totalDespesas) {
		super();
		this.id = id;
		this.ano = ano;
		this.mes = mes;
		this.totalReceitas = totalReceitas != null ? totalReceitas : 0d;
		this.totalDespesas = totalDespesas != null ? totalDespesas : 0d;
	}






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
	
	public String getAnoMes(){
		StringBuilder sb = new StringBuilder();
		if(this.mes != null){
			sb.append(this.mes);
		}
		sb.append("/");
		if(this.ano != null){
			sb.append(this.ano);
		}
		return sb.toString();
	}

	public Double getTotalReceitas() {
		return totalReceitas;
	}

	public void setTotalReceitas(Double totalReceitas) {
		this.totalReceitas = totalReceitas;
	}

	public Double getTotalDespesas() {
		return totalDespesas;
	}

	public void setTotalDespesas(Double totalDespesas) {
		this.totalDespesas = totalDespesas;
	}
	
	public void zerarTotais(){
		this.totalDespesas = 0d;
		this.totalReceitas = 0d;
	}

}