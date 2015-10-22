package br.com.virtz.condominio.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.entidades.Votacao;
import br.com.virtz.condominio.entidades.Voto;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.exceptions.CondominioException;
import br.com.virtz.condominio.geral.ParametroSistemaLookup;
import br.com.virtz.condominio.service.IVotacaoService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class ListagemVotacaoController {
	
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
	
	
	private List<Votacao> votacoes;
	private List<Voto> votos = null;
	private Map<String, Integer> resultadoVotacaoSelecionada;
	
	
	@PostConstruct
	public void init(){
		Usuario usuario = sessao.getUsuarioLogado();
		votacoes = votacaoService.recuperarTodas(usuario.getCondominio());
		resultadoVotacaoSelecionada = new HashMap<String, Integer>();
	}
	
	public void ativarVotacao(Votacao votacao) throws CondominioException{
		try {
			votacaoService.ativarVotacao(votacao);
		} catch (AppException e) {
			throw new CondominioException(e.getMessage());
		}
		message.addInfo("A votação foi ativada!");
	}
	
	public void desativarVotacao(Votacao votacao) throws CondominioException{
		try {
			votacaoService.desativarVotacao(votacao);
		} catch (AppException e) {
			throw new CondominioException(e.getMessage());
		}
		message.addInfo("A votação foi desativada!");
	}
	
	public void excluirVotacao(Votacao votacao) throws CondominioException{
		try {
			votacaoService.removerVotacao(votacao);
		} catch (AppException e) {
			throw new CondominioException(e.getMessage());
		}
		message.addInfo("A votação foi excluída!");
	}
	
	public void irParaCadastro(){
		navigation.redirectToPage("/votacao/cadastrarVotacao.faces");
	}
	
	public void editar(Votacao votacao){
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("idVotacao", votacao.getId());
		navigation.redirectToPage("/votacao/cadastrarVotacao.faces");
	}
	
	
	public boolean estaAtiva(Votacao votacao){
		if(votacao != null){
			return "ATIVA".equals(votacao.qualStatus());
		}
		return Boolean.FALSE;
	}
	
	
	public boolean estaInativa(Votacao votacao){
		if(votacao != null){
			return "INATIVA".equals(votacao.qualStatus());
		}
		return Boolean.TRUE;
	}
	
	
	public boolean estaEncerrada(Votacao votacao){
		if(votacao != null){
			return "ENCERRADA".equals(votacao.qualStatus());
		}
		return Boolean.FALSE;
	}
	
	
	public boolean possoRemover(Votacao votacao){
		if(votacao != null){
			if(!votacao.isAtiva()  && !estaEncerrada(votacao)) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}
	
	
	public boolean possoEditar(Votacao votacao){
		if(votacao != null){
			if(!estaEncerrada(votacao) && votacaoService.totalVotos(votacao) == 0) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}
	
	
	public void verResultado(Votacao votacao){
		if(votacao != null){
			try {
				resultadoVotacaoSelecionada = votacaoService.recuperarResultado(votacao.getId());
			} catch (AppException e) {
				resultadoVotacaoSelecionada = null;
				e.printStackTrace();
			}
		}
	}
	

	public void carregarVotos(Votacao votacaoSelecionada) {
		try {
			votos = votacaoService.recuperarTodosVotos(votacaoSelecionada.getId());
		} catch (AppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			votos = null;
		}
	}
	
	
	
	
	/* GETTERS E SETTERS*/

	public List<Votacao> getVotacoes() {
		return votacoes;
	}

	public void setVotacoes(List<Votacao> votacoes) {
		this.votacoes = votacoes;
	}

	public Map<String, Integer> getResultadoVotacaoSelecionada() {
		return resultadoVotacaoSelecionada;
	}

	public void setResultadoVotacaoSelecionada(
			Map<String, Integer> resultadoVotacaoSelecionada) {
		this.resultadoVotacaoSelecionada = resultadoVotacaoSelecionada;
	}

	public List<Voto> getVotos() {
		return votos;
	}
	
	


}

