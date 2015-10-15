package br.com.virtz.condominio.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import br.com.virtz.condominio.entidades.Recebido;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.entidades.Votacao;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.exceptions.CondominioException;
import br.com.virtz.condominio.geral.ParametroSistemaLookup;
import br.com.virtz.condominio.service.IRecebidoService;
import br.com.virtz.condominio.service.IVotacaoService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class ListagemCorrespondenciaController {
	
	@EJB
	private IRecebidoService recebidoService;
	
	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private MessageHelper message;
	
	@Inject
	private NavigationPage navigation;
	
	
	private List<Recebido> recebidos;
	
	private Usuario usuario = null;
	
	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
		recebidos = recebidoService.recuperarPorApartamento(usuario.getApartamento().getId());
	}

	
	
	public List<Recebido> getRecebidos() {
		return recebidos;
	}
	
	
	
}

