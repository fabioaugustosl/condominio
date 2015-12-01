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
import javax.xml.bind.annotation.XmlRootElement;

import br.com.virtz.condominio.constantes.EnumTaxaCreditoDebito;
import br.com.virtz.condominio.constantes.EnumTaxaPorcentagemValor;

@Entity(name="taxa")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "Taxa.recuperarPorCondominio", 
			query = "Select u FROM taxa u "
					+ " WHERE u.condominio.id = :idCondominio ")
})
public class Taxa extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "descricao", length = 100)
	private String descricao;

	@Column(name = "creditoOuDebito", length = 1)
	@Enumerated(EnumType.STRING)
	private EnumTaxaCreditoDebito creditoOuDebito; // C - credito | D - debito

	@Column(name = "porcentagemOuValor", length = 1)
	@Enumerated(EnumType.STRING)
	private EnumTaxaPorcentagemValor porcentagemOuValor;  // P - porcentagem | V = valor

	@Column(name = "valor")
	private Double valor;
	
	@ManyToOne
	@JoinColumn(name="idCondominio")
	private Condominio condominio;

	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public EnumTaxaCreditoDebito getCreditoOuDebito() {
		return creditoOuDebito;
	}

	public void setCreditoOuDebito(EnumTaxaCreditoDebito creditoOuDebito) {
		this.creditoOuDebito = creditoOuDebito;
	}

	public EnumTaxaPorcentagemValor getPorcentagemOuValor() {
		return porcentagemOuValor;
	}

	public void setPorcentagemOuValor(EnumTaxaPorcentagemValor porcentagemOuValor) {
		this.porcentagemOuValor = porcentagemOuValor;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}
	
	public Condominio getCondominio() {
		return condominio;
	}

	public void setCondominio(Condominio condominio) {
		this.condominio = condominio;
	}
	
	public String getCreditoOuDebitoExibicao(){
		if(this.creditoOuDebito != null){
			this.creditoOuDebito.getDescricao();
		}
		return null;
	}
	
	public String getPorcentagemOuValorExibicao() {
		if(this.porcentagemOuValor != null){
			this.porcentagemOuValor.getDescricao();
		}
		return null;
	}
	
}