package br.com.virtz.condominio.entidades;

import java.io.Serializable;

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
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name="avaliacao")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name="Avaliacao.recuperarAvalicaoPorBatepapoEUsuario",
				query="Select a FROM avaliacao a WHERE a.usuario.id = :idUsuario and a.batePapo.id = :idBatePapo")
})
public class Avaliacao extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "positiva", nullable = true, columnDefinition = "TINYINT", length = 1)
	private Boolean positiva;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "idUsuario", nullable = false)
	private Usuario usuario;

	@Column(name = "observacao", length = 500)
	private String observacao;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idBatePapo")
	private BatePapo batePapo; 

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean isPositiva() {
		return positiva;
	}

	public void setPositiva(Boolean positiva) {
		this.positiva = positiva;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		observacao = observacao;
	}

	public BatePapo getBatePapo() {
		return batePapo;
	}

	public void setBatePapo(BatePapo batePapo) {
		this.batePapo = batePapo;
	}
	

}