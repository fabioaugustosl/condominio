package br.com.virtz.condominio.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name="batepapo")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name="BatePapo.recuperarTodosPorCondominio",
				query="SELECT distinct b FROM batepapo b LEFT JOIN FETCH b.avaliacoes a WHERE b.condominio.id = :idCondominio ORDER BY b.id DESC")
})
public class BatePapo extends Entidade implements Serializable, Comparable<BatePapo> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "idUsuario", nullable = false)
	private Usuario usuario;

	@ManyToOne
	@JoinColumn(name = "idCondominio", nullable = false)
	private Condominio condominio;

	@Column(name = "mensagem", length = 800)
	private String mensagem;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data")
	private Date data;

	@OneToMany(mappedBy="batePapo")
	private Set<Avaliacao> avaliacoes;
	
	@OneToMany(mappedBy="batePapo")
	private Set<ComentarioBatePapo> comentarios;
	
	@Transient
	private int qtdPositivas = 0;
	
	@Transient
	private int qtdNegativas = 0;

	
	
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

	public Condominio getCondominio() {
		return condominio;
	}

	public void setCondominio(Condominio condominio) {
		this.condominio = condominio;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public Set<Avaliacao> getAvaliacoes() {
		return avaliacoes;
	}

	public void setAvaliacoes(Set<Avaliacao> avaliacoes) {
		this.avaliacoes = avaliacoes;
		calcularAvalicoesPositivasENegativas();
	}

	public int getQtdPositivas() {
		return qtdPositivas;
	}

	public int getQtdNegativas() {
		return qtdNegativas;
	}
	
	public Set<ComentarioBatePapo> getComentarios() {
		return comentarios;
	}

	public void setComentarios(Set<ComentarioBatePapo> comentarios) {
		this.comentarios = comentarios;
	}
	
	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public void calcularAvalicoesPositivasENegativas(){
		qtdPositivas = 0;
		qtdNegativas = 0;
		
		for(Avaliacao a : getAvaliacoes()){
			if(a.isPositiva()){
				qtdPositivas++;
			} else {
				qtdNegativas++;
			}
		}

	}

	@Override
	public int compareTo(BatePapo o) {
		if(o == null){
			return 1;
		}
		if(this.getId() == null && o.getId() != null){
			return -11;
		} else if(this.getId() == null && o.getId() == null){
			return 0;
		}
		
		return o.getId().compareTo(this.getId());
	}

}