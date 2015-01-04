package br.com.virtz.condominio.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import br.com.virtz.condominio.constantes.EnumTipoVotacao;
import br.com.virtz.condominio.controller.util.UtilTipoVotacao;
import br.com.virtz.condominio.entidades.OpcaoVotacao;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.entidades.Votacao;
import br.com.virtz.condominio.exceptions.CondominioException;
import br.com.virtz.condominio.geral.ParametroSistemaLookup;
import br.com.virtz.condominio.service.IVotacaoService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class CadastroVotacaoController {
	
	@EJB
	private IVotacaoService votacaoService;
	
	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private ParametroSistemaLookup parametroLookup; 
	
	@Inject
	private MessageHelper message;
	
	@Inject
	private NavigationPage navigation;
	
		
	private Votacao votacao;
	private List<String> tiposVotacao;
	private String tipoVotacaoSelecionado;
	private UtilTipoVotacao utilTipoVotacao;
	private String descricaoNovaOpcao;
	
	
	@PostConstruct
	public void init(){
		Usuario usuario = sessao.getUsuarioLogado();
		montarComboTipoVotacao();
		utilTipoVotacao = new UtilTipoVotacao();
		votacao = votacaoService.criarNovaVotacao(usuario, usuario.getCondominio(), null, null);
	}

	
	private void montarComboTipoVotacao(){
		tiposVotacao = new ArrayList<String>();
		for(EnumTipoVotacao tipoVotacao : EnumTipoVotacao.values()){
			tiposVotacao.add(tipoVotacao.getDescricao());
		}
	}

	
	public void montarOpcaoTipoVotacao(){
		if(StringUtils.isNotBlank(tipoVotacaoSelecionado)){
			EnumTipoVotacao tipoVotacao = EnumTipoVotacao.recuperarPorDescricao(tipoVotacaoSelecionado);
			utilTipoVotacao.tratarTipoVotacaoExibicao(tipoVotacao);
		}
	}
	
	
	public void adicionarOpcao(ActionEvent event){
		if(StringUtils.isNotBlank(descricaoNovaOpcao)){
			OpcaoVotacao opcao = votacao.adicionarNovaOpcao(descricaoNovaOpcao);
		}
		descricaoNovaOpcao = null;
	}

	
	public void removerOpcao(OpcaoVotacao opcaoRemover){
		votacaoService.removerOpcao(opcaoRemover);
		votacao.removerOpcao(opcaoRemover);
	}
	
	
	public void salvarVotacao(ActionEvent event) throws CondominioException {
		if(votacao == null){
			throw new CondominioException("Nenhuma votação encontrada para ser salva.");
		}
		
		votacao.setTipoVotacao(EnumTipoVotacao.recuperarPorDescricao(getTipoVotacaoSelecionado()));
		
		if(this.isOpcoes() && (votacao.getOpcoes()== null || votacao.getOpcoes().isEmpty()) && votacao.getOpcoes().size() < 2){
			throw new CondominioException("Você selecionou o tipo de votação por opções, porém devem ser cadastradas no mínimo duas (2) opções.");
		}
		
		if(votacao.getDataLimite().before(new Date())){
			throw new CondominioException("A data limite da votação deve ser maior que a data atual.");
		}
		
		try{
			votacaoService.salvarVotacao(votacao);
			message.addInfo("A nova votação foi criada com sucesso.");
			
			votacao = votacaoService.criarNovaVotacao(sessao.getUsuarioLogado(), sessao.getUsuarioLogado().getCondominio(), null, null);
		}catch(Exception e){
			e.printStackTrace();
			throw new CondominioException("Ocorreu um erro inesperado ao salvar a nova votação. Favor tente novamente.");
		}
	}
	
	public void irParaListagem(){
		navigation.redirectToPage("/votacao/listagemVotacao.faces");
	}
	
	/* GETTERS E SETTERS*/
		
	public Votacao getVotacao() {
		return votacao;
	}
	
	public void setVotacao(Votacao votacao) {
		this.votacao = votacao;
	}

	public List<String> getTiposVotacao() {
		return tiposVotacao;
	}

	public String getTipoVotacaoSelecionado() {
		return tipoVotacaoSelecionado;
	}

	public void setTipoVotacaoSelecionado(String tipoVotacaoSelecionado) {
		this.tipoVotacaoSelecionado = tipoVotacaoSelecionado;
	}

	public boolean isSimNao() {
		return utilTipoVotacao.isSimNao();
	}

	public boolean isData() {
		return utilTipoVotacao.isData();
	}

	public boolean isMoeda() {
		return utilTipoVotacao.isMoeda();
	}

	public boolean isNumerico() {
		return utilTipoVotacao.isNumerico();
	}

	public boolean isOpcoes() {
		return utilTipoVotacao.isOpcoes();
	}

	public UtilTipoVotacao getUtilTipoVotacao() {
		return utilTipoVotacao;
	}

	public String getDescricaoNovaOpcao() {
		return descricaoNovaOpcao;
	}

	public void setDescricaoNovaOpcao(String descricaoNovaOpcao) {
		this.descricaoNovaOpcao = descricaoNovaOpcao;
	}
	
}

