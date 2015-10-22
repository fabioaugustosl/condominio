package br.com.virtz.condominio.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import br.com.virtz.condominio.constantes.EnumTipoNotificacao;
import br.com.virtz.condominio.constantes.EnumTipoRecebido;
import br.com.virtz.condominio.entidades.Apartamento;
import br.com.virtz.condominio.entidades.Bloco;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exceptions.CondominioException;
import br.com.virtz.condominio.service.ICondominioService;
import br.com.virtz.condominio.service.INotificacaoService;
import br.com.virtz.condominio.service.IRecebidoService;
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
	
		
}
