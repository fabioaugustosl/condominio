package br.com.virtz.condominio.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.virtz.condominio.constantes.EnumTipoVotacao;

@Entity(name="votacao")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name="Votacao.recuperarPorCondominio",
				query="Select v FROM votacao v WHERE v.condominio.id = :idCondominio ORDER BY v.dataLimite DESC, v.encerrada ASC "),
	@NamedQuery(name="Votacao.recuperarAtivasPorCondominio",
				query="Select v FROM votacao v WHERE v.condominio.id = :idCondominio AND v.ativa = 1 AND v.encerrada = 0 ORDER BY v.dataLimite DESC"),
	@NamedQuery(name="Votacao.recuperarAtivasValidasPorCondominio",
				query="Select v FROM votacao v "
						+ " WHERE v.condominio.id = :idCondominio AND v.ativa = 1 AND v.encerrada = 0 AND (v.dataLimite = NULL OR v.dataLimite >= :dataLimite)"
						+ " ORDER BY v.dataLimite DESC"),
	@NamedQuery(name="Votacao.recuperarEncerradasSemEnvioEmail",
				query="Select v FROM votacao v "
						+ " WHERE v.ativa = 1 "
						+ " AND v.emailEnviado = 0 AND (v.encerrada = 1 OR v.dataLimite < :dataLimite)"
						+ " ORDER BY v.dataLimite DESC"),
	@NamedQuery(name="Votacao.recuperarNovasSemEnvioEmail",
				query="Select v FROM votacao v "
						+ " WHERE v.ativa = 1 "
						+ " AND v.emailNovaEnviado = 0 AND v.encerrada = 0 "
						+ " AND (v.dataLimite = null OR v.dataLimite >= :dataHoje)"
						+ " ORDER BY v.dataLimite DESC")
})
public class Votacao extends Entidade implements Serializable,Comparable<Votacao>  {

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

	@Column(name = "assuntoVotacao", length = 2000, nullable = false)
	private String assuntoVotacao;

	@Enumerated(EnumType.STRING)
	@Column(name = "tipoVotacao", length = 20, nullable = false)
	private EnumTipoVotacao tipoVotacao;

	@Column(name = "dataLimite")
	private Date dataLimite;

	@Column(name = "ativa")
	private boolean ativa;

	@Column(name = "encerrada")
	private boolean encerrada;

	@Column(name = "emailEnviado")
	private boolean emailEnviado;

	@Column(name = "emailNovaEnviado")
	private boolean emailNovaEnviado;

	@Column(name = "resultadoParcial")
	private boolean resultadoParcial;

	@OneToMany(mappedBy="votacao", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private List<OpcaoVotacao> opcoes;

	@OneToMany(mappedBy="votacao", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private Set<OpcaoVotacaoComImagem> opcoesComImagem;

	@OneToMany(mappedBy="votacao", cascade=CascadeType.REMOVE)
	private List<Voto> votos;

	@Column(name = "votacaoOficial")
	private Boolean votacaoOficial;



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

	public String getAssuntoVotacao() {
		return assuntoVotacao;
	}

	public void setAssuntoVotacao(String assuntoVotacao) {
		this.assuntoVotacao = assuntoVotacao;
	}

	public EnumTipoVotacao getTipoVotacao() {
		return tipoVotacao;
	}

	public void setTipoVotacao(EnumTipoVotacao tipoVotacao) {
		this.tipoVotacao = tipoVotacao;
	}

	public Date getDataLimite() {
		return dataLimite;
	}

	public void setDataLimite(Date dataLimite) {
		this.dataLimite = dataLimite;
	}

	public List<OpcaoVotacao> getOpcoes() {
		return opcoes;
	}

	public void setOpcoes(List<OpcaoVotacao> opcoes) {
		this.opcoes = opcoes;
	}

	public List<Voto> getVotos() {
		return votos;
	}

	public void setVotos(List<Voto> votos) {
		this.votos = votos;
	}

	public boolean isAtiva() {
		return ativa;
	}

	public void setAtiva(boolean ativa) {
		this.ativa = ativa;
	}

	public boolean isResultadoParcial() {
		return resultadoParcial;
	}

	public void setResultadoParcial(boolean resultadoParcial) {
		this.resultadoParcial = resultadoParcial;
	}

	public Boolean getVotacaoOficial() {
		return votacaoOficial == null ? Boolean.FALSE : votacaoOficial;
	}

	public void setVotacaoOficial(Boolean votacaoOficial) {
		this.votacaoOficial = votacaoOficial;
	}

	public boolean isEncerrada() {
		return encerrada;
	}

	public void setEncerrada(boolean encerrada) {
		this.encerrada = encerrada;
	}

	public Set<OpcaoVotacaoComImagem> getOpcoesComImagem() {
		return opcoesComImagem;
	}

	public void setOpcoesComImagem(Set<OpcaoVotacaoComImagem> opcoesComImagem) {
		this.opcoesComImagem = opcoesComImagem;
	}

	public boolean isEmailNovaEnviado() {
		return emailNovaEnviado;
	}

	public void setEmailNovaEnviado(boolean emailNovaEnviado) {
		this.emailNovaEnviado = emailNovaEnviado;
	}

	public OpcaoVotacao adicionarNovaOpcao(String descricao){
		OpcaoVotacao opcao = new OpcaoVotacao();
		opcao.setDescricao(descricao);
		opcao.setVotacao(this);

		if(getOpcoes() == null){
			setOpcoes(new ArrayList<OpcaoVotacao>());
		}
		getOpcoes().add(opcao);
		return opcao;
	}

	public void removerOpcao(OpcaoVotacao opcao){

		if(getOpcoes() != null && !getOpcoes().isEmpty()){
			getOpcoes().remove(opcao);
		}

	}

	public OpcaoVotacaoComImagem adicionarNovaOpcaoComImagem(String descricao, ArquivoOpcaoVotacao arquivo, ArquivoOpcaoVotacao arquivoThumb){
		OpcaoVotacaoComImagem opcao = new OpcaoVotacaoComImagem();
		opcao.setDescricao(descricao);
		opcao.setVotacao(this);
		opcao.setImagem(arquivo);
		opcao.setImagemThumb(arquivoThumb);

		if(getOpcoesComImagem() == null){
			setOpcoesComImagem(new HashSet<OpcaoVotacaoComImagem>());
		}

		getOpcoesComImagem().add(opcao);

		return opcao;
	}

	public void removerOpcaoComImagem(OpcaoVotacaoComImagem opcao){

		if(getOpcoesComImagem() != null && !getOpcoesComImagem().isEmpty()){
			getOpcoesComImagem().remove(opcao);
		}

	}

	public String qualStatus(){
		if(this.ativa && !estaEncerrada()) {
			return "ATIVA";
		} else if(!this.ativa && !estaEncerrada()) {
			return "INATIVA";
		} else {
			return "ENCERRADA";
		}
	}

	public boolean estaEncerrada(){

		if(this.encerrada){
			return Boolean.TRUE;
		}

		Date hoje = new Date();
		if(this.getDataLimite() != null && hoje.after(this.getDataLimite())){
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public boolean isEmailEnviado() {
		return emailEnviado;
	}

	public void setEmailEnviado(boolean emailEnviado) {
		this.emailEnviado = emailEnviado;
	}

	@Override
	public int compareTo(Votacao o) {
		if(o == null){
			return 1;
		}


		if(this.dataLimite != null && o.getDataLimite() == null){
			return 1;
		} else if(this.dataLimite == null && o.getDataLimite() != null){
			return -1;
		} else if(this.dataLimite != null && o.getDataLimite() != null){
			return o.getDataLimite().compareTo(this.dataLimite);
		}
		return 0;
	}

	public boolean isVotacaoPorOpcaoImagem(){
		if(tipoVotacao != null && EnumTipoVotacao.OPCOES_IMAGEM.equals(tipoVotacao)){
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

}