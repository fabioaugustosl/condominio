package br.com.virtz.condominio.controller;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.model.LazyDataModel;

import br.com.virtz.condominio.controller.lazy.VisitantesLazyModel;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.entidades.Visitante;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.service.IVisitanteService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class ListagemVisitanteController {

	@EJB
	private IVisitanteService visitanteService;

	@Inject
	private SessaoUsuario sessao;

	@Inject
	private MessageHelper message;

	@Inject
	private NavigationPage navigation;



	private LazyDataModel<Visitante> visitantes = null;

	private Usuario usuario = null;



	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
		//visitantes = new  VisitantesLazyModel(visitanteService.recuperarPorCondominioPaginado(usuario.getCondominio().getId(), 0, 50), usuario.getCondominio().getId(), visitanteService);
	}

	public LazyDataModel<Visitante> getVisitantes(){
		if(visitantes == null){
			visitantes = new VisitantesLazyModel(visitanteService.recuperarPorCondominioPaginado(usuario.getCondominio().getId(), 0, 50), usuario.getCondominio().getId(), visitanteService);
		}
		return visitantes;
	}


	public void irParaCadastro(){
		navigation.redirectToPage("/portaria/cadastrarVisitante.faces");
	}

	
	public void notificarSaida(Long idVisitante){
		try {
			visitanteService.saidaVisitante(idVisitante);
			visitantes = new VisitantesLazyModel(visitanteService.recuperarPorCondominioPaginado(usuario.getCondominio().getId(), 0, 50), usuario.getCondominio().getId(), visitanteService);
			this.message.addInfo("Saída registrada!");
		} catch (AppException e) {
			e.printStackTrace();
		}
	}



	/* GETTERS E SETTERS*/



}

