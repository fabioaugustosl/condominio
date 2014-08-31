package br.com.virtz.condominio.controller;

import java.awt.event.ActionEvent;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.virtz.condominio.entity.Bloco;
import br.com.virtz.condominio.service.IBlocoService;

@ManagedBean
@ViewScoped
public class BlocoController {

	@EJB
	private IBlocoService blocoService;
	
	private Bloco bloco;
	
	@PostConstruct
	public void init(){
		bloco = new Bloco();
	}
	
	public void salvar(ActionEvent event){
		//TODO
	}

		
	// GETTERS E SETTERS
	public Bloco getBloco() {
		return bloco;
	}

	public void setBloco(Bloco bloco) {
		this.bloco = bloco;
	}
	
}
