package br.com.virtz.condominio.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.virtz.condominio.constantes.EnumFuncionalidadeAtalho;
import br.com.virtz.condominio.constantes.EnumPeriodicidadeLembrete;

@Entity
@Table(name="lembrete_sindico")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "LembreteSindico.recuperarPorCondominio",
				query = "Select n FROM LembreteSindico n "
						+ " WHERE n.condominio.id = :idCondominio and n.ativo = true "
						+ " ORDER BY n.id DESC ")
})
public class LembreteSindico extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "idCondominio")
	private Condominio condominio;

	@Column(name = "texto", length=500)
	private String texto;
	
	@Column(name = "linkImagem", length=500)
	private String linkImagem;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "periodicidade", length=100)
	private EnumPeriodicidadeLembrete periodicidade;

	@Column(name = "ativo")
	private Boolean ativo;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "dataInicio")
	private Date dataInicio;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "dataUltimoEnvio")
	private Date dataUltimoEnvio;
	
	

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

	public String getLinkImagem() {
		return linkImagem;
	}

	public void setLinkImagem(String linkImagem) {
		this.linkImagem = linkImagem;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public EnumPeriodicidadeLembrete getPeriodicidade() {
		return periodicidade;
	}

	public void setPeriodicidade(EnumPeriodicidadeLembrete periodicidade) {
		this.periodicidade = periodicidade;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataUltimoEnvio() {
		return dataUltimoEnvio;
	}

	public void setDataUltimoEnvio(Date dataUltimoEnvio) {
		this.dataUltimoEnvio = dataUltimoEnvio;
	}
	

}