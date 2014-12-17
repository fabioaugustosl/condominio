package br.com.virtz.condominio.entidades;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@NamedQueries({
	@NamedQuery(name="BatePapo.recuperarTodosPorCondominio",
				query="SELECT distinct b FROM BatePapo b JOIN FETCH b.avaliacoes a WHERE b.condominio.id = :idCondominio")
})
public class BatePapo extends Entidade implements Serializable {

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

	@OneToMany(mappedBy="batePapo")
//	@JoinTable(name = "AvaliacaoBatePapo", joinColumns = { @JoinColumn(name = "idBatePapo") }, inverseJoinColumns = { @JoinColumn(name = "idAvaliacao") })
	private Set<Avaliacao> avaliacoes;
	
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

}