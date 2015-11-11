package br.com.virtz.condominio.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.virtz.boleto.bean.EnumBanco;

@Entity
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "ContaBancariaCondominio.recuperarPorCondominio", 
			query = "Select u FROM ContaBancariaCondominio u "
					+ " WHERE u.condominio.id = :idCondominio ")
})
public class ContaBancariaCondominio extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "banco", nullable = false, length=50)
	private EnumBanco banco;

	@NotNull
	@Column(name = "agencia", length = 10, nullable = false)
	private String agencia;
	
	@NotNull
	@Column(name = "numeroConta", length = 20, nullable = false)
	private String numeroConta;

	@Column(name = "digitoAgencia", length = 2)
	private String digitoAgencia;
	
	@Column(name = "digitoConta", length = 2)
	private String digitoConta;

	@Column(name = "codigoCarteira", length = 10)
	private String codigoCarteira;
	
	@ManyToOne
	@JoinColumn(name="idCondominio")
	private Condominio condominio;

	
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EnumBanco getBanco() {
		return banco;
	}

	public void setBanco(EnumBanco banco) {
		this.banco = banco;
	}

	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public String getDigitoAgencia() {
		return digitoAgencia;
	}

	public void setDigitoAgencia(String digitoAgencia) {
		this.digitoAgencia = digitoAgencia;
	}

	public String getCodigoCarteira() {
		return codigoCarteira;
	}

	public void setCodigoCarteira(String codigoCarteira) {
		this.codigoCarteira = codigoCarteira;
	}

	public Condominio getCondominio() {
		return condominio;
	}

	public void setCondominio(Condominio condominio) {
		this.condominio = condominio;
	}

	public String getNumeroConta() {
		return numeroConta;
	}

	public void setNumeroConta(String numeroConta) {
		this.numeroConta = numeroConta;
	}

	public String getDigitoConta() {
		return digitoConta;
	}

	public void setDigitoConta(String digitoConta) {
		this.digitoConta = digitoConta;
	}
	
	
}