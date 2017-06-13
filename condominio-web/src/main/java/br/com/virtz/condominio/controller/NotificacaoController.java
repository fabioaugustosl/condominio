package br.com.virtz.condominio.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;

import br.com.virtz.condominio.entidades.Notificacao;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.service.INotificacaoService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.MessageHelper;

@ManagedBean
@RequestScoped
public class NotificacaoController {

	@EJB
	private INotificacaoService notificacaoService;
	
	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private MessageHelper messageHelper;

	
	
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



	public void excluir(Notificacao notificacao){
		 if(notificacao != null){
			 notificacoes.remove(notificacao);
			 try {
				notificacaoService.remover(notificacao.getId());
			} catch (AppException e) {
				messageHelper.addError("Aconteceu algum erro ao remover a notificação.");
			}
		 }
	}
	 
	 
	
	// getters e setters
	
	public List<Notificacao> getNotificacoes() {
		return notificacoes;
	}

	public int getQtdNotificacoes() {
		return qtdNotificacoes;
	}


}
