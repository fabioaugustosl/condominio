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
		@NamedQuery(name = "CobrancaUsuario.recuperarPorCondominio", 
				query = "Select u FROM CobrancaUsuario u "
						+ " JOIN u.configuracaoBoleto conf "
						+ " WHERE conf.condominio.id = :idCondominio "),
		@NamedQuery(name = "CobrancaUsuario.recuperarPorCondominioUsuarioAnoMes", 
				query = "Select u FROM CobrancaUsuario u "
						+ " JOIN u.configuracaoBoleto conf "
						+ " WHERE u.usuario.id = :idUsuario AND conf.condominio.id = :idCondominio "
						+ " AND u.ano = :ano AND u.mes = :mes " ),
		@NamedQuery(name = "CobrancaUsuario.recuperarPorCondominioAnoMes", 
				query = "Select u FROM CobrancaUsuario u "
						+ " JOIN u.configuracaoBoleto conf "
						+ " WHERE conf.condominio.id = :idCondominio AND u.usuario is not null "
						+ " AND u.ano = :ano AND u.mes = :mes " ),
		@NamedQuery(name = "CobrancaUsuario.recuperarPorCondominioAnoMesTodosAvulsos", 
				query = "Select u FROM CobrancaUsuario u "
						+ " JOIN u.configuracaoBoleto conf "
						+ " WHERE conf.condominio.id = :idCondominio AND u.avulso is not null "
						+ " AND u.ano = :ano AND u.mes = :mes " ),				
		@NamedQuery(name = "CobrancaUsuario.recuperarPorCondominioAvulsoAnoMes", 
				query = "Select u FROM CobrancaUsuario u "
						+ " JOIN u.configuracaoBoleto conf "
						+ " WHERE u.avulso.id = :idAvulso AND conf.condominio.id = :idCondominio "
						+ " AND u.ano = :ano AND u.mes = :mes "),
		@NamedQuery(name = "CobrancaUsuario.recuperarAnosMesesPorCondominio", 
				query = "Select c.ano, c.mes FROM CobrancaUsuario c "
						+ " JOIN c.configuracaoBoleto conf "
						+ " WHERE conf.condominio.id = :idCondominio "
						+ " GROUP BY c.ano, c.mes ORDER BY c.ano DESC, c.mes DESC ")
})
public class CobrancaUsuario extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "valor")
	private Double valor;

	@Column(name = "nossoNumero", length = 20)
	private String nossoNumero;

	@Column(name = "digitoNossoNumero", length = 5)
	private String digitoNossoNumero;

	@Column(name = "codigoBarras", length = 60)
	private String codigoBarras;

	@Column(name = "ano", length = 4)
	private Integer ano;

	@Column(name = "mes", length = 2)
	private Integer mes;

	@ManyToOne
	@JoinColumn(name = "idAvulso")
	private Avulso avulso;

	@ManyToOne
	@JoinColumn(name = "idUsuario")
	private Usuario usuario;

	@ManyToOne
	@JoinColumn(name = "idConfiguracaoBoleto", nullable = false)
	private ConfiguracaoBoleto configuracaoBoleto;
	
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public String getNossoNumero() {
		return nossoNumero;
	}

	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}

	public String getDigitoNossoNumero() {
		return digitoNossoNumero;
	}

	public void setDigitoNossoNumero(String digitoNossoNumero) {
		this.digitoNossoNumero = digitoNossoNumero;
	}

	public String getCodigoBarras() {
		return codigoBarras;
	}

	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Avulso getAvulso() {
		return avulso;
	}

	public void setAvulso(Avulso avulso) {
		this.avulso = avulso;
	}

	public ConfiguracaoBoleto getConfiguracaoBoleto() {
		return configuracaoBoleto;
	}

	public void setConfiguracaoBoleto(ConfiguracaoBoleto configuracaoBoleto) {
		this.configuracaoBoleto = configuracaoBoleto;
	}
	

}