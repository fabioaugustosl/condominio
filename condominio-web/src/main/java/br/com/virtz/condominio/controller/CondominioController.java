package br.com.virtz.condominio.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import br.com.virtz.condominio.entity.Bloco;
import br.com.virtz.condominio.service.IBlocoService;
import br.com.virtz.condominio.util.MessageHelper;

@ManagedBean
@ViewScoped
public class CondominioController {

	@EJB
	private IBlocoService blocoService;
	
	@Inject
	private MessageHelper message;
	
	private List<Bloco> blocos;
	private String nomeCondominio;
	private Integer quantidadeBlocos;
	
	@PostConstruct
	public void init(){
		// TODO: recuperar o nome do condominio do usuario logado
		nomeCondominio = "Ponto Imperial";
		
		blocos = listarTodos(); 
	}
	
	
	public List<Bloco> listarTodos(){
		return blocoService.recuperarTodos();
	}
	

	// GETTERS E SETTERS
	
	public List<Bloco> getBlocos() {
		return blocos;
	}

	public void setBlocos(List<Bloco> blocos) {
		this.blocos = blocos;
	}

	public String getNomeCondominio() {
		return nomeCondominio;
	}

	public Integer getQuantidadeBlocos() {
		return quantidadeBlocos;
	}

	public void setQuantidadeBlocos(Integer quantidadeBlocos) {
		this.quantidadeBlocos = quantidadeBlocos;
	}
		
}
