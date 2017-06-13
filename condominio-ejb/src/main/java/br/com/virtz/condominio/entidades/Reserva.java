package br.com.virtz.condominio.entidades;

import java.io.Serializable;
import java.util.Calendar;

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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name="reserva")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "Reserva.recuperarQtdReservasPorArea", 
			query = "Select count(*) FROM reserva r "
					+ "	WHERE r.areaComum.id = :idAreaComum "),
	@NamedQuery(name = "Reserva.recuperarPorAreaNomeEData", 
			query = "Select r FROM reserva r "
					+ "	WHERE r.areaComum.id = :idAreaComum AND r.usuario.nome = :nomeUsuario AND r.data = :data "),
	@NamedQuery(name = "Reserva.recuperarPorAreaEmailEData", 
			query = "Select r FROM reserva r "
					+ "	WHERE r.areaComum.id = :idAreaComum AND r.usuario.email = :emailUsuario AND r.data = :data "),
	@NamedQuery(name = "Reserva.recuperarPorAreaAptoEData", 
			query = "Select r FROM reserva r "
					+ "	WHERE r.areaComum.id = :idAreaComum AND r.apartamento.id = :idApartamento AND r.data = :data "),
	@NamedQuery(name = "Reserva.recuperarPorAreaEEmail", 
			query = "Select r FROM reserva r "
					+ "	WHERE r.areaComum.id = :idAreaComum AND r.usuario.email = :emailUsuario "),
	@NamedQuery(name = "Reserva.recuperarPorAreaEApto", 
			query = "Select r FROM reserva r "
						+ "	WHERE r.areaComum.id = :idAreaComum AND r.apartamento.id = :idApartamento "),
	@NamedQuery(name = "Reserva.recuperarPorAreaAPartirMesAno", 
			query = "Select r FROM reserva r "
						+ "	WHERE r.areaComum.id = :idAreaComum "
						+ " AND YEAR(r.data) >= :ano AND MONTH(r.data) >= :mes ")
})
public class Reserva extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Temporal(TemporalType.DATE)
	@NotNull
	@Column(name = "data", nullable = false)
	private Calendar data;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "horaInicio")
	private Calendar horaInicio;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "horaFim")
	private Calendar horaFim;

	@ManyToOne
	@JoinColumn(name = "idAreaComum")
	private AreaComum areaComum;

	@ManyToOne
	@JoinColumn(name = "idUsuario")
	private Usuario usuario;
	
	@ManyToOne
	@JoinColumn(name = "idApartamento")
	private Apartamento apartamento;
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Calendar getData() {
		return data;
	}

	public void setData(Calendar data) {
		this.data = data;
	}

	public AreaComum getAreaComum() {
		return areaComum;
	}

	public void setAreaComum(AreaComum areaComum) {
		this.areaComum = areaComum;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Calendar getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(Calendar horaInicio) {
		this.horaInicio = horaInicio;
	}

	public Calendar getHoraFim() {
		return horaFim;
	}

	public void setHoraFim(Calendar horaFim) {
		this.horaFim = horaFim;
	}

	public Apartamento getApartamento() {
		return apartamento;
	}

	public void setApartamento(Apartamento apartamento) {
		this.apartamento = apartamento;
	}

}