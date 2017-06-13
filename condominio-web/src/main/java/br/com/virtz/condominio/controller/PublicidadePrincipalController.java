package br.com.virtz.condominio.controller;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import br.com.virtz.condominio.constantes.EnumFuncionalidadeAtalho;
import br.com.virtz.condominio.constantes.EnumTipoPublicidade;
import br.com.virtz.condominio.entidades.Atalho;
import br.com.virtz.condominio.entidades.Publicidade;
import br.com.virtz.condominio.service.IAtalhoService;
import br.com.virtz.condominio.service.IPublicidadeService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class PublicidadePrincipalController {
	
	@EJB
	private IPublicidadeService publicidadeService;
	
	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private NavigationPage navegacao;

	
	private Publicidade publicidadeDireita;
	
	
	@PostConstruct
	public void init(){
		publicidadeDireita = publicidadeService.recuperar(sessao.getUsuarioLogado().getCondominio().getId(), EnumTipoPublicidade.PORTAL_DIREITO_SUPERIOR); 
	}
	
	
	public boolean possuiPublicidadeDireita(){
		if(publicidadeDireita != null){
			return true;
		}
		return false;
	}
	

	public void irPublicidadeDireita(){
		
		if(publicidadeDireita != null && publicidadeDireita.getLink() != null){
			navegacao.forwardToPage(publicidadeDireita.getLink());
		}
	}
	
	
	// GETTERS E SETTERS


	public IPublicidadeService getPublicidadeService() {
		return publicidadeService;
	}


	public Publicidade getPublicidadeDireita() {
		return publicidadeDireita;
	}
	
	

	
}
