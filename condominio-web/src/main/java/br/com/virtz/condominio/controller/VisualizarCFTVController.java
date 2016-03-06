package br.com.virtz.condominio.controller;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import br.com.virtz.condominio.entidades.AcessoCFTV;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.service.ICondominioService;
import br.com.virtz.condominio.session.SessaoUsuario;

@ManagedBean
@ViewScoped
public class VisualizarCFTVController {
	
	@EJB
	private ICondominioService condominioService;
	
	@Inject
	private SessaoUsuario sessao;
	
	
	private Usuario usuario = null;
	
	private AcessoCFTV cftv = null;
	
	
	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
		
		// cftv
		cftv = condominioService.recuperarCFTV(usuario.getCondominio().getId());
		
		if(cftv == null){
			cftv = new AcessoCFTV();
		}
		
	}
	
	public String getURL(){
		if(cftv != null){
			return cftv.getUrl();
		}
		return null;
	}

	// GETTERS E SETTERS

	public AcessoCFTV getCftv() {
		return cftv;
	}

}
