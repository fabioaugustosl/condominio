package br.com.virtz.condominio.controller;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import br.com.virtz.condominio.constantes.EnumTipoUsuario;
import br.com.virtz.condominio.entidades.Avaliacao;
import br.com.virtz.condominio.entidades.BatePapo;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.Notificacao;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.ErroAoSalvar;
import br.com.virtz.condominio.service.IBatePapoService;
import br.com.virtz.condominio.service.INotificacaoService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.MessageHelper;

@ManagedBean
@ViewScoped
public class NotificacaoController {

	@EJB
	private INotificacaoService notificacaoService;
	
	@Inject
	private SessaoUsuario sessao;
	
	private List<Notificacao> notificacoes = null;
	private Usuario usuario = null;
	private int qtdNotificacoes;
	
	@PostConstruct
	public void init(){
		qtdNotificacoes = 0;
		usuario = sessao.getUsuarioLogado();
		notificacoes = listarTodos(usuario);
		if(notificacoes != null & !notificacoes.isEmpty()){
			qtdNotificacoes = notificacoes.size();
		}
	}
	
	
	public List<Notificacao> listarTodos(Usuario usuario){
		
		List<Notificacao> notificacoes = notificacaoService.recuperar(usuario.getCondominio().getId(), usuario.getId());
		
		return notificacoes;
	}


	
	
	// getters e setters
	
	public List<Notificacao> getNotificacoes() {
		return notificacoes;
	}

	public int getQtdNotificacoes() {
		return qtdNotificacoes;
	}


}
