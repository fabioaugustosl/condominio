package br.com.virtz.condominio.entidades;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name="visitante")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "Visitante.recuperarPorCondominio", 
					query = "SELECT v FROM visitante v "
								+ " WHERE v.condominio.id = :idCondominio  "
								+ " ORDER BY v.id DESC"),
				@NamedQuery(name = "Visitante.totalVisitantes", 
					query = "SELECT count(v) FROM visitante v WHERE v.condominio.id = :idCondominio   "),
				@NamedQuery(name = "Visitante.recuperarPorApartamento", 
					query = "SELECT v FROM visitante v "
								+ " WHERE v.apartamento.id in (:idApartamento)  "
								+ " ORDER BY v.id DESC"),
				@NamedQuery(name = "Visitante.totalVisitantesApartamento", 
					query = "SELECT count(v) FROM visitante v WHERE v.apartamento.id in (:idApartamento)   ")
				
})
public class Visitante extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "idApartamento")
	private Apartamento apartamento;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "idCondominio")
	private Condominio condominio;

	@Column(name = "nome", length = 100)
	private String nome;
	
	@Column(name = "empresa", length = 200)
	private String empresa;
	
	@Column(name = "documento", length = 50)
	private String documento;
	
	@Column(name = "comentario", length = 2000)
	private String comentario;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dataEntrada")
	private Date dataEntrada;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dataSaida")
	private Date dataSaida;
	
	@Column(name = "foto", length = 100)
	private String foto;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Apartamento getApartamento() {
		return apartamento;
	}

	public void setApartamento(Apartamento apartamento) {
		this.apartamento = apartamento;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmpresa() {
		return empresa;
	}

	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public Date getDataEntrada() {
		return dataEntrada;
	}

	public void setDataEntrada(Date dataEntrada) {
		this.dataEntrada = dataEntrada;
	}

	public Date getDataSaida() {
		return dataSaida;
	}

	public void setDataSaida(Date dataSaida) {
		this.dataSaida = dataSaida;
	}

	public Condominio getCondominio() {
		return condominio;
	}

	public void setCondominio(Condominio condominio) {
		this.condominio = condominio;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}
	
}