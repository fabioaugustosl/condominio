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
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.virtz.condominio.constantes.EnumFuncaoBloqueio;

@Entity
@Table(name = "bloqueioFuncaoUsuario")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "BloqueioFuncaoUsuario.recuperarPorFuncaoUsuario",
				query = "SELECT i FROM BloqueioFuncaoUsuario i "
						+ " JOIN i.usuario u "
						+ " WHERE u.id = :idUsuario AND i.funcaoBloqueio = :funcaoBloqueio"),
		@NamedQuery(name = "BloqueioFuncaoUsuario.recuperarPorFuncaoECondominio",
				query = "SELECT i FROM BloqueioFuncaoUsuario i "
						+ " JOIN i.usuario u "
						+ " WHERE u.condominio.id = :idCondominio AND i.funcaoBloqueio = :funcaoBloqueio"),
		@NamedQuery(name = "BloqueioFuncaoUsuario.recuperarPorUsuario",
				query = "SELECT i FROM BloqueioFuncaoUsuario i "
						+ " JOIN i.usuario u "
						+ " WHERE u.id = :idUsuario ")
})
public class BloqueioFuncaoUsuario extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "idUsuario", nullable = false)
	private Usuario usuario;

	@Column(name = "comentario", nullable = true)
	private String comentario;

	@Enumerated(EnumType.STRING)
	@Column(name = "funcao", nullable = false)
	private EnumFuncaoBloqueio funcaoBloqueio;

	@Transient
	private boolean marcado = true;


	public BloqueioFuncaoUsuario() {
		super();
	}

	public BloqueioFuncaoUsuario(Long id, Long idUsuario, String comentario,
			EnumFuncaoBloqueio funcaoBloqueio) {
		super();

		this.id = id;
		this.usuario = new Usuario();
		this.usuario.setId(idUsuario);
		this.comentario = comentario;
		this.funcaoBloqueio = funcaoBloqueio;
		if(id != null){
			this.marcado =true;
		}
	}

	public BloqueioFuncaoUsuario(Long id, String comentario,
			EnumFuncaoBloqueio funcaoBloqueio) {
		super();
		this.id = id;
		this.comentario = comentario;
		this.funcaoBloqueio = funcaoBloqueio;
		if(id != null){
			this.marcado =true;
		}
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public EnumFuncaoBloqueio getFuncaoBloqueio() {
		return funcaoBloqueio;
	}

	public void setFuncaoBloqueio(EnumFuncaoBloqueio funcaoBloqueio) {
		this.funcaoBloqueio = funcaoBloqueio;
	}

	public boolean isMarcado() {
		return marcado;
	}

	public void setMarcado(boolean marcado) {
		this.marcado = marcado;
	}


}