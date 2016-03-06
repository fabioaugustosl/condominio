package br.com.virtz.condominio.entidades;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="opcaovotacaoimagem")
@XmlRootElement
public class OpcaoVotacaoComImagem extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "descricao", length = 2000, nullable = false)
	private String descricao;

	@ManyToOne
	@JoinColumn(name = "idVotacao")
	private Votacao votacao;

	@OneToOne(fetch= FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="idImagem")
	private ArquivoOpcaoVotacao imagem;
	
	@OneToOne(fetch= FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="idImagemThumb")
	private ArquivoOpcaoVotacao imagemThumb;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Votacao getVotacao() {
		return votacao;
	}

	public void setVotacao(Votacao votacao) {
		this.votacao = votacao;
	}
	
	public ArquivoOpcaoVotacao getImagem() {
		return imagem;
	}

	public void setImagem(ArquivoOpcaoVotacao imagem) {
		this.imagem = imagem;
	}

	public ArquivoOpcaoVotacao getImagemThumb() {
		return imagemThumb;
	}

	public void setImagemThumb(ArquivoOpcaoVotacao imagemThumb) {
		this.imagemThumb = imagemThumb;
	}

	@Override
	public boolean equals(Object opcao) {
		if(opcao == null){
			return Boolean.FALSE;
		}
		if(!(opcao instanceof OpcaoVotacaoComImagem)){
			return Boolean.FALSE;
		}
		OpcaoVotacaoComImagem opc = (OpcaoVotacaoComImagem) opcao;
		if(opc.getId() != null){
			return opc.getId().equals(this.getId());
		}
		if(opc.getDescricao() != null){
			return opc.getDescricao().equals(this.getDescricao());
		}
		return Boolean.FALSE;
	}
	
}