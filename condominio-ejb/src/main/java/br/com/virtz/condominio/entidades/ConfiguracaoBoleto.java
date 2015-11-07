package br.com.virtz.condominio.entidades;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "ConfiguracaoBoleto.recuperarPorCondominio", 
						query = "Select u FROM ConfiguracaoBoleto u "
							+ " WHERE u.condominio.id = :idCondominio "),
				@NamedQuery(name = "ConfiguracaoBoleto.recuperarPorCondominioAnoMes", 
							query = "Select u FROM ConfiguracaoBoleto u "
								+ " WHERE u.condominio.id = :idCondominio AND u.ano = :ano AND u.mes = :mes ")
})
public class ConfiguracaoBoleto extends Entidade implements Serializable {

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

	@Column(name = "aceite")
	private Boolean aceite;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dataVencimento")
	private Date dataVencimento;

	@Column(name = "instrucaoLocalPgto")
	private String instrucaoLocalPgto;

	@Column(name = "instrucaoSacado")
	private String instrucaoSacado;

	@Column(name = "instrucoesGerais")
	private String instrucoesGerais;

	@Column(name = "valorMultaAposVencimento")
	private Double valorMultaAposVencimento;

	@Column(name = "valorJurosDiariosAposVencimento")
	private Double valorJurosDiariosAposVencimento;

	@Column(name = "bloquearEdicao")
	private Boolean bloquearEdicao;
	
	@Column(name = "valorBase")
	private Double valorBase;

	@OneToMany(mappedBy="configuracaoBoleto")
	private List<TaxaConfiguracaoBoleto> taxas;

	@OneToMany(mappedBy="configuracaoBoleto")
	private List<TaxaExtraMensal> taxasExtras;

	@OneToMany(mappedBy="configuracaoBoleto")
	private List<CobrancaUsuario> cobrancas;

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

	public Boolean getAceite() {
		return aceite;
	}

	public void setAceite(Boolean aceite) {
		this.aceite = aceite;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public String getInstrucaoLocalPgto() {
		return instrucaoLocalPgto;
	}

	public void setInstrucaoLocalPgto(String instrucaoLocalPgto) {
		this.instrucaoLocalPgto = instrucaoLocalPgto;
	}

	public String getInstrucaoSacado() {
		return instrucaoSacado;
	}

	public void setInstrucaoSacado(String instrucaoSacado) {
		this.instrucaoSacado = instrucaoSacado;
	}

	public String getInstrucoesGerais() {
		return instrucoesGerais;
	}

	public void setInstrucoesGerais(String instrucoesGerais) {
		this.instrucoesGerais = instrucoesGerais;
	}

	
	public Double getValorMultaAposVencimento() {
		return valorMultaAposVencimento;
	}

	public void setValorMultaAposVencimento(Double valorMultaAposVencimento) {
		this.valorMultaAposVencimento = valorMultaAposVencimento;
	}

	public Double getValorJurosDiariosAposVencimento() {
		return valorJurosDiariosAposVencimento;
	}

	public void setValorJurosDiariosAposVencimento(
			Double valorJurosDiariosAposVencimento) {
		this.valorJurosDiariosAposVencimento = valorJurosDiariosAposVencimento;
	}

	public void setBloquearEdicao(Boolean bloquearEdicao) {
		this.bloquearEdicao = bloquearEdicao;
	}

	public Boolean getBloquearEdicao() {
		return bloquearEdicao;
	}

	public List<TaxaExtraMensal> getTaxasExtras() {
		return taxasExtras;
	}

	public void setTaxasExtras(List<TaxaExtraMensal> taxasExtras) {
		this.taxasExtras = taxasExtras;
	}

	public List<CobrancaUsuario> getCobrancas() {
		return cobrancas;
	}

	public void setCobrancas(List<CobrancaUsuario> cobrancas) {
		this.cobrancas = cobrancas;
	}

	public List<TaxaConfiguracaoBoleto> getTaxas() {
		return taxas;
	}

	public void setTaxas(List<TaxaConfiguracaoBoleto> taxas) {
		this.taxas = taxas;
	}

	public Double getValorBase() {
		return valorBase;
	}

	public void setValorBase(Double valorBase) {
		this.valorBase = valorBase;
	}
		

}