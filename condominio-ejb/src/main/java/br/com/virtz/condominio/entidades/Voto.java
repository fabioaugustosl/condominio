package br.com.virtz.condominio.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@NamedQueries({
	@NamedQuery(name="Voto.totalVotoPorVotacao",
				query="Select count(*) FROM Voto v WHERE v.votacao.id = :idVotacao ")
})
public class Voto extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "sim")
	private boolean sim;

	@Column(name = "data")
	private boolean data;

	@Column(name = "dataInicio")
	private boolean dataInicio;

	@Column(name = "dataFim")
	private boolean dataFim;

	@Column(name = "valor")
	private boolean valor;

	@Column(name = "total")
	private boolean total;
	
	@Column(name = "opcao")
	private int opcao;
	
	@ManyToOne
	@JoinColumn(name = "idVotacao")
	private Votacao votacao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isSim() {
		return sim;
	}

	public void setSim(boolean sim) {
		this.sim = sim;
	}

	public boolean isData() {
		return data;
	}

	public void setData(boolean data) {
		this.data = data;
	}

	public boolean isDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(boolean dataInicio) {
		this.dataInicio = dataInicio;
	}

	public boolean isDataFim() {
		return dataFim;
	}

	public void setDataFim(boolean dataFim) {
		this.dataFim = dataFim;
	}

	public boolean isValor() {
		return valor;
	}

	public void setValor(boolean valor) {
		this.valor = valor;
	}

	public boolean isTotal() {
		return total;
	}

	public void setTotal(boolean total) {
		this.total = total;
	}

	public int getOpcao() {
		return opcao;
	}

	public void setOpcao(int opcao) {
		this.opcao = opcao;
	}

	public Votacao getVotacao() {
		return votacao;
	}

	public void setVotacao(Votacao votacao) {
		this.votacao = votacao;
	}
	
}