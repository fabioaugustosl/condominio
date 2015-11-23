package br.com.virtz.condominio.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import br.com.virtz.condominio.entidades.Balanco;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.service.IBalancoService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class ListagemBalancoController {
	
	@EJB
	private IBalancoService balancoService;
	
	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private MessageHelper message;
	
	@Inject
	private NavigationPage navigation;
	
	
	private List<Balanco> balancos = null;
	private Usuario usuario = null;
	
	
	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
		balancos = balancoService.recuperarPorCondominioComSomatorio(usuario.getCondominio().getId());
	}
	

	public void detalhar(Long idBalanco){
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("idBalanco", idBalanco);
		navigation.redirectToPage("/balanco/detalharBalanco.faces");
	}


	
	
	
	/* GETTERS E SETTERS*/
	public List<Balanco> getBalancos() {
		return balancos;
	}
	
}

