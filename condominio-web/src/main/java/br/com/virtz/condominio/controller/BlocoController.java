package br.com.virtz.condominio.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.com.virtz.condominio.entity.Bloco;
import br.com.virtz.condominio.service.IBlocoService;

@ManagedBean
@ViewScoped
public class BlocoController {

	@EJB
	private IBlocoService blocoService;
	
	private Bloco bloco;
	private Bloco blocoSelecionado;
	private List<Bloco> blocos;
	
	@PostConstruct
	public void init(){
		bloco = new Bloco();
		blocoSelecionado = null;
		blocos = listarTodos(); 
	}
	
	public void salvar(){
		try {
			blocoService.salvar(bloco);
			blocos = listarTodos(); 
			bloco = new Bloco();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("O bloco "+bloco.getNome()+" foi salvo com sucesso!"));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocorreu um erro ao salvar o bloco", null));
		}
	}
	
	public void remover(){
		
		if(blocoSelecionado == null){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nenhum bloco foi selecionado para ser removido.", null));
		}
		
		blocoService.remover(blocoSelecionado.getId());
		blocos = listarTodos(); 
		blocoSelecionado = null;
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "O bloco "+bloco.getNome()+" foi removido com sucesso", null));
	}
	
	public List<Bloco> listarTodos(){
		return blocoService.recuperarTodos();
	}
	

	// GETTERS E SETTERS
	public Bloco getBloco() {
		return bloco;
	}

	public void setBloco(Bloco bloco) {
		this.bloco = bloco;
	}

	public Bloco getBlocoSelecionado() {
		return blocoSelecionado;
	}

	public void setBlocoSelecionado(Bloco blocoSelecionado) {
		this.blocoSelecionado = blocoSelecionado;
	}

	public List<Bloco> getBlocos() {
		return blocos;
	}

	public void setBlocos(List<Bloco> blocos) {
		this.blocos = blocos;
	}	
	
}
