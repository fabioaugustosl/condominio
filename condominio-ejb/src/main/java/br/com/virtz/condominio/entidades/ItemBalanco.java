package br.com.virtz.condominio.entidades;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

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
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.virtz.condominio.constantes.EnumTipoBalanco;

@Entity(name="itembalanco")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "ItemBalanco.recuperarUltimasDescricoes", 
						query = "Select distinct(i.descricao) FROM itembalanco i "
								+ " JOIN i.balanco b "
							+ " WHERE b.condominio.id = :idCondominio AND i.tipoBalanco = :tipo "
							+ " AND b.ano <= :ano "
							+ " ORDER BY b.ano DESC, b.mes DESC ")
})
public class ItemBalanco extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "idBalanco", nullable = false)
	private Balanco balanco;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data", nullable = false)
	private Date data;

	@Column(name = "descricao", nullable = false)
	private String descricao;

	@Column(name = "valor", nullable = false, precision=10, scale=2)
	private Double valor;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "tipoBalanco", nullable = false)
	private EnumTipoBalanco tipoBalanco;
	
	@OneToOne(fetch= FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="idArquivo")
	private ArquivoBalanco arquivo;

	@ManyToOne
	@JoinColumn(name = "idUsuario")
	private Usuario usuario;
	
	@ManyToOne
	@JoinColumn(name = "idCategoria")
	private CategoriaItemBalanco categoria;

	
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Balanco getBalanco() {
		return balanco;
	}

	public void setBalanco(Balanco balanco) {
		this.balanco = balanco;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public EnumTipoBalanco getTipoBalanco() {
		return tipoBalanco;
	}

	public void setTipoBalanco(EnumTipoBalanco tipoBalanco) {
		this.tipoBalanco = tipoBalanco;
	}

	public ArquivoBalanco getArquivo() {
		return arquivo;
	}

	public void setArquivo(ArquivoBalanco arquivo) {
		this.arquivo = arquivo;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public CategoriaItemBalanco getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaItemBalanco categoria) {
		this.categoria = categoria;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ItemBalanco)){
			return false;
		}
		ItemBalanco c = (ItemBalanco) obj;
		if(c.getId() != null){
			return super.equals(obj);
		}
		if(this.getDescricao().equals(c.getDescricao()) && this.getTipoBalanco().equals(c.getTipoBalanco()) && this.getData().equals(c.getData()) && this.getValor().equals(c.getValor())){
			return true;
		}
		return false;
	}
	
	public String getValorFormatado(){
		NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
		return formatter.format(this.valor);
	}


}