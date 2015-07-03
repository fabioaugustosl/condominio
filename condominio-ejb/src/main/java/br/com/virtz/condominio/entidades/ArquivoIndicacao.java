package br.com.virtz.condominio.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class ArquivoIndicacao extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "idIndicacao", nullable = false)
	private Indicacao indicacao;

	@Column(name = "caminho", length=500)
	private String caminho;
	
	@Column(name = "tamanho")
	private long tamanho;
	
	@Column(name = "nome", length=100, nullable = false)
	private String nome;

	@Column(name = "nomeOriginal", length=500)
	private String nomeOriginal;
	
	@Column(name = "extensao", length=5)
	private String extensao;
	
	@Column(name = "destaque")
	private Boolean destaque;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Indicacao getIndicacao() {
		return indicacao;
	}

	public void setIndicacao(Indicacao indicacao) {
		this.indicacao = indicacao;
	}

	public void setDestaque(Boolean destaque) {
		this.destaque = destaque;
	}

	public String getCaminho() {
		return caminho;
	}

	public void setCaminho(String caminho) {
		this.caminho = caminho;
	}

	public long getTamanho() {
		return tamanho;
	}

	public void setTamanho(long tamanho) {
		this.tamanho = tamanho;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNomeOriginal() {
		return nomeOriginal;
	}

	public void setNomeOriginal(String nomeOriginal) {
		this.nomeOriginal = nomeOriginal;
	}

	public String getExtensao() {
		return extensao;
	}

	public void setExtensao(String extensao) {
		this.extensao = extensao;
	}

	public boolean getDestaque() {
		return destaque == null ? Boolean.FALSE : destaque;
	}

	public void setDestaque(boolean destaque) {
		this.destaque = destaque;
	}
	
	public String getCaminhoCompleto(){
		return "/arquivos/"+getNome();
	}
	
	@Override
	public boolean equals(Object obj) {
		ArquivoIndicacao a = (ArquivoIndicacao) obj;
		if(this.getId() == null || a.getId() == null){
			return this.getNomeOriginal().endsWith(a.getNomeOriginal());
		}
		return this.getId().equals(a.getId());
	}
	
}