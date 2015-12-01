package br.com.virtz.condominio.entidades;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.virtz.condominio.constantes.EnumTipoVotacao;

@Entity(name="voto")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "Voto.totalVotoPorVotacao", query = "Select count(*) FROM voto v WHERE v.votacao.id = :idVotacao "),
		@NamedQuery(name = "Voto.recuperarPorUsuario", query = "Select v FROM voto v WHERE v.usuario.id = :idUsuario AND v.votacao.id = :idVotacao "),
		@NamedQuery(name = "Voto.recuperarPorApto", 
					query = "Select v FROM voto v "
							+ " JOIN v.usuario u "
							+ " JOIN u.apartamento ap "
							+ "	WHERE v.votacao.id = :idVotacao AND ap.id = :idApto"),
		@NamedQuery(name = "Voto.recuperarPorVotacao", 
					query = "Select v FROM voto v "
							+ " JOIN FETCH v.usuario u "
							+ " JOIN FETCH u.apartamento ap "
							+ "	WHERE v.votacao.id = :idVotacao ")})
public class Voto extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "sim")
	private Boolean sim;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data")
	private Date data;

	@Column(name = "moeda")
	private Double moeda;

	@Column(name = "numero")
	private Double numero;

	@ManyToOne
	@JoinColumn(name = "idOpcao")
	private OpcaoVotacao opcao;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dataVotacao")
	private Date dataVotacao;

	@ManyToOne
	@JoinColumn(name = "idUsuario")
	private Usuario usuario;

	@ManyToOne
	@JoinColumn(name = "idVotacao")
	private Votacao votacao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean isSim() {
		return sim;
	}

	public void setSim(Boolean sim) {
		this.sim = sim;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Double getMoeda() {
		return moeda;
	}

	public void setMoeda(Double moeda) {
		this.moeda = moeda;
	}

	public Double getNumero() {
		return numero;
	}

	public void setNumero(Double numero) {
		this.numero = numero;
	}

	public OpcaoVotacao getOpcao() {
		return opcao;
	}

	public void setOpcao(OpcaoVotacao opcao) {
		this.opcao = opcao;
	}

	public Date getDataVotacao() {
		return dataVotacao;
	}

	public void setDataVotacao(Date dataVotacao) {
		this.dataVotacao = dataVotacao;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Votacao getVotacao() {
		return votacao;
	}

	public void setVotacao(Votacao votacao) {
		this.votacao = votacao;
	}
	
	/**
	 * De acordo com o tipo de votacao retorna um object. Quem receber deve fazer o cast.
	 * @return
	 */
	public Object getOpcaoVotada(EnumTipoVotacao tipoVotacao){
		if(tipoVotacao.equals(EnumTipoVotacao.SIM_NAO)){
			return isSim();
		} else if(tipoVotacao.equals(EnumTipoVotacao.DATA)) {
			return getData();
		} else if(tipoVotacao.equals(EnumTipoVotacao.MOEDA)) {
			return getMoeda();
		} else if(tipoVotacao.equals(EnumTipoVotacao.NUMERICA)) {
			return getNumero();
		} else if(tipoVotacao.equals(EnumTipoVotacao.OPCOES)) {
			return getOpcao();
		}
		return null;
	}

	/**
	 * De acordo com o tipo de votacao retorna um object. Quem receber deve fazer o cast.
	 * @return
	 */
	public String getOpcaoVotadaString(EnumTipoVotacao tipoVotacao){
		if(tipoVotacao.equals(EnumTipoVotacao.SIM_NAO)){
			return (isSim()) ? "Sim" : "Não" ;
		} else if(tipoVotacao.equals(EnumTipoVotacao.DATA)) {
			return getData().toString();
		} else if(tipoVotacao.equals(EnumTipoVotacao.MOEDA)) {
			return getMoeda().toString();
		} else if(tipoVotacao.equals(EnumTipoVotacao.NUMERICA)) {
			return getNumero().toString();
		} else if(tipoVotacao.equals(EnumTipoVotacao.OPCOES)) {
			return getOpcao().getDescricao();
		}
		return null;
	}
	
}