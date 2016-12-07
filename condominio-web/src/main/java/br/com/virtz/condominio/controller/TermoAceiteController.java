package br.com.virtz.condominio.controller;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exceptions.CondominioException;
import br.com.virtz.condominio.service.ICondominioService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class TermoAceiteController {

	@EJB
	private ICondominioService condominioService;

	@Inject
	private SessaoUsuario sessao;

	@Inject
	private MessageHelper message;

	@Inject
	private NavigationPage navigation;




	@PostConstruct
	public void init(){
	}


	public void salvar(ActionEvent event) throws CondominioException {
		try{
			Usuario u = sessao.getUsuarioLogado();
			Condominio cond = u.getCondominio();
			cond.setDataAceiteTermo(new Date());
			cond.setUsuarioTermoAceite(u.getEmail()+ " (id : "+u.getId()+")");

			cond = condominioService.salvar(cond);
			sessao.getUsuarioLogado().setCondominio(cond);
			sair();
		}catch(Exception e){
			e.printStackTrace();
			throw new CondominioException("Ocorreu um erro inesperado ao salvar termo de aceite");
		}
	}

	public void sair(){
		navigation.redirectToPage("/portal.faces");
	}


	/* GETTERS e SETTERS*/


}
