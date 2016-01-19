package br.com.virtz.condominio.controller;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;

import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@RequestScoped
public class ControlePortariaController {
	
	
	@Inject
	private NavigationPage navegacao;
	
	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private MessageHelper message;
	
	@PostConstruct
	public void init(){
	}
	
	
	public void irParaCadastroRecebido(){
		navegacao.redirectToPage("/portaria/cadastrarRecebido.faces");
	}
	
		
	public void irParaCadastroVisitante(){
		navegacao.redirectToPage("/portaria/cadastrarVisitante.faces");
	}
	
}
