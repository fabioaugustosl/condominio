package br.com.virtz.condominio.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.StringEscapeUtils;

@Entity
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "Noticia.recuperarPorCondominio", 
				query = "Select n FROM Noticia n WHERE n.condominio.id = :idCondominio "),
		@NamedQuery(name = "Noticia.recuperarPorCondominioAtivas", 
				query = "Select n FROM Noticia n WHERE n.condominio.id = :idCondominio AND n.ativa = 1 ORDER BY n.id DESC")
})
public class Noticia extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "ativa")
	private boolean ativa;
	
	@Column(name = "destaque")
	private boolean destaque;

	@Temporal(TemporalType.DATE)
	@Column(name = "data")
	private Date data;

	@ManyToOne
	@JoinColumn(name = "idCondominio")
	private Condominio condominio;
	
	@Column(name = "titulo", length=400)
	private String titulo;
	
	@Column(name = "conteudo", length=100000)
	private String conteudo;
	
	@OneToMany(mappedBy="noticia", fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	private List<ArquivoNoticia> arquivos;

	
	
	public Noticia() {
		super();
		arquivos = new ArrayList<ArquivoNoticia>();
	}

	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isAtiva() {
		return ativa;
	}

	public void setAtiva(boolean ativa) {
		this.ativa = ativa;
	}

	public boolean isDestaque() {
		return destaque;
	}

	public void setDestaque(boolean destaque) {
		this.destaque = destaque;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Condominio getCondominio() {
		return condominio;
	}

	public void setCondominio(Condominio condominio) {
		this.condominio = condominio;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public List<ArquivoNoticia> getArquivos() {
		return arquivos;
	}

	public void setArquivos(List<ArquivoNoticia> arquivos) {
		this.arquivos = arquivos;
	}
	
	
	public ArquivoNoticia getArquivoNoticiaDestaque(){
		if(this.arquivos != null && !this.arquivos.isEmpty()){
			for(ArquivoNoticia arq : this.arquivos){
				if(arq.getDestaque()){
					return arq;
				}
			}
			return this.arquivos.get(0);
		}
		return null;
	}
	
	/**
	 * Exibe apenas os primeiros 400 caracteres.
	 * @return
	 */
	public String getConteudoReduzido() {
		if(this.conteudo == null){
			return "";
		}
		
		if(this.conteudo.length() < 140){
			return this.conteudo;
		}
		return this.conteudo.substring(0, 140);
	}
	
	
}