package br.com.virtz.condominio.controller;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import br.com.virtz.condominio.constantes.EnumFuncionalidadeAtalho;
import br.com.virtz.condominio.entidades.Atalho;
import br.com.virtz.condominio.service.IAtalhoService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class AtalhoPrincipalControllerController {
	
	@EJB
	private IAtalhoService atalhoService;
	
	@Inject
	private SessaoUsuario sessao;

	@Inject
	private NavigationPage navegacao;
	
	
	
	private Atalho atalhoNotificacao;
	
	@PostConstruct
	public void init(){
		
		atalhoNotificacao = atalhoService.recuperarTodos(sessao.getUsuarioLogado().getCondominio(), EnumFuncionalidadeAtalho.NOTIFICAR_PORTARIA);
		
	}
	
	
	public boolean possuiAtalhoNotificacao(){
		if(atalhoNotificacao != null){
			return true;
		}
		return false;
	}

	
	public void irParaNotificacao(){
		navegacao.redirectToPage("/portaria/cadastrarNotificacaoPortaria.faces");
	}
	
	
	
	// GETTERS E SETTERS

	public Atalho getAtalhoNotificacao() {
		return atalhoNotificacao;
	}

	
}
