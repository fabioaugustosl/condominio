package br.com.virtz.condominio.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.model.LazyDataModel;

import br.com.virtz.condominio.controller.lazy.VisitantesMoradorLazyModel;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.entidades.Visitante;
import br.com.virtz.condominio.service.PaginacaoServiceVisitantes;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class ListagemVisitanteMoradorController {

	@EJB
	private PaginacaoServiceVisitantes visitanteService;

	@Inject
	private SessaoUsuario sessao;

	@Inject
	private MessageHelper message;



	private List<Visitante> visitantes = null;

	private Usuario usuario = null;



	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
		visitantes = visitanteService.recuperarPorApartamentoPaginado(usuario.getApartamento().getId(), 0, 200);
	}


	
	/* GETTERS E SETTERS*/

	public List<Visitante> getVisitantes() {
		return visitantes;
	}

}

