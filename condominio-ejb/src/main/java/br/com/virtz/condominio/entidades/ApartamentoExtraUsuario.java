package br.com.virtz.condominio.entidades;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="apartamento_extra_usuario")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "ApartamentoExtraUsuario.recuperarPorUsuario",
				query = "Select n FROM ApartamentoExtraUsuario n "
						+ " JOIN n.usuario u "
						+ " JOIN FETCH n.apartamento a "
						+ " JOIN FETCH a.bloco  "
						+ " WHERE u.id = :idUsuario ORDER BY n.id DESC ")
})
public class ApartamentoExtraUsuario extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "idApartamento")
	private Apartamento apartamento;

	@ManyToOne
	@JoinColumn(name = "idUsuario")
	private Usuario usuario;

	
	


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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}


}