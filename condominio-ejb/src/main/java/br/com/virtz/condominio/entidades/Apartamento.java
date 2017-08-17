package br.com.virtz.condominio.entidades;

import java.io.Serializable;
import java.util.List;

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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


@Entity(name="apartamento")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "Apartamento.recuperarPorCondominioENumeroBloco",
						query = "Select a FROM apartamento a "
							+ " LEFT JOIN a.bloco b "
							+ " WHERE b.condominio.id = :idCondominio AND a.numero = :numero AND b.nome = :nomeBloco"),
				@NamedQuery(name = "Apartamento.recuperarNaoAssociadosPorCondominio",
						query = "Select a FROM apartamento a "
							+ " LEFT JOIN a.usuario u "
							+ " LEFT JOIN a.bloco b "
							+ " WHERE b.condominio.id = :idCondominio AND u is null "),
				@NamedQuery(name = "Apartamento.recuperarPorCondominioENumeroBlocoAgrupamento",
						query = "Select a FROM apartamento a "
							+ " LEFT JOIN a.bloco b "
							+ " LEFT JOIN b.agrupamentoUnidades agrupamento "
							+ " WHERE b.condominio.id = :idCondominio AND a.numero = :numero AND b.nome = :nomeBloco AND agrupamento.nome = :nomeAgrupamento")
})
public class Apartamento extends Entidade implements Serializable, Comparable<Apartamento> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	
	@ManyToOne
	@JoinColumn(name="idBloco", nullable=true)
	private Bloco bloco;

	@Column
	private Integer andar;

	@Column
	private String numero;

	@XmlTransient
	@OneToMany(mappedBy="apartamento")
	private List<Usuario> usuario;



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Bloco getBloco() {
		return bloco;
	}

	public void setBloco(Bloco bloco) {
		this.bloco = bloco;
	}

	public Integer getAndar() {
		return andar;
	}

	public void setAndar(Integer andar) {
		this.andar = andar;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getNomeExibicao(){
		if(this.bloco.getCondominio().condominioEhVertical()){
			return this.numero+"  [Andar: "+this.andar+"]";
		} else {
			return this.numero;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null){
			return false;
		}

		if (!(obj instanceof Entidade))
			return false;

		if (obj == this)
			return true;

		if(this.getId() != null){
			return this.getId().equals(((Apartamento)obj).getId());
		}

		if(this.getNumero() != null){
			return this.getNumero().equals(((Apartamento)obj).getNumero());
		}

		return true;
	}

	@Override
	public int hashCode() {
	    int hash = 7;

	    if(this.getId() != null){
	    	 hash = 23 * hash + (this.getId()!= null ? this.getId().hashCode() : 0);
		} else if(this.getNumero() != null){
			 hash = 23 * hash + (this.getNumero()!= null ? this.getNumero().hashCode() : 0);
		}

	    return hash;
	}

	@Override
	public int compareTo(Apartamento o) {
		if(o == null){
			return 1;
		}
		return this.getNumero().compareTo(o.getNumero());
	}

	public List<Usuario> getUsuario() {
		return usuario;
	}

	public void setUsuario(List<Usuario> usuario) {
		this.usuario = usuario;
	}
	
	
}