package br.com.virtz.condominio.entidades;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.virtz.condominio.constantes.EnumTipoRecebido;

@Entity(name="recebido")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "Recebido.recuperarPorApto", 
				query = "SELECT n FROM recebido n "
					+ " WHERE n.apartamento.id = :idApartamento  "
					+ " ORDER BY n.data DESC"),
				@NamedQuery(name = "Recebido.recuperarPorCondominio", 
				query = "SELECT r FROM recebido r "
							+ " JOIN r.apartamento a "
							+ " JOIN a.bloco b "
							+ " WHERE b.condominio.id = :idCondominio  "
							+ " ORDER BY r.data DESC")	
})
public class Recebido extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "idApartamento", nullable = false)
	private Apartamento apartamento;

	@Column(name = "comentario", length = 2000)
	private String comentario;

	@Enumerated(EnumType.STRING)
	@Column(name = "tipoRecebido", length = 20, nullable = false)
	private EnumTipoRecebido tipoRecebido;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data")
	private Date data;

	
	
	
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

	public EnumTipoRecebido getTipoRecebido() {
		return tipoRecebido;
	}

	public void setTipoRecebido(EnumTipoRecebido tipoRecebido) {
		this.tipoRecebido = tipoRecebido;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

}