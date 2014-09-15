package br.com.virtz.condominio.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.event.FlowEvent;
import org.primefaces.event.RowEditEvent;

import br.com.virtz.condominio.entity.Bloco;
import br.com.virtz.condominio.entity.Condominio;
import br.com.virtz.condominio.entity.Usuario;
import br.com.virtz.condominio.service.IBlocoService;
import br.com.virtz.condominio.service.ICondominioService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.MessageHelper;

@ManagedBean
@ViewScoped
public class CondominioController {

	@EJB
	private IBlocoService blocoService;
	
	@EJB
	private ICondominioService condominioService;
	
	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private MessageHelper message;
	
	private List<Bloco> blocos;
	private Condominio condominio;
	private Usuario usuario;
	private Integer quantidadeBlocos = null;
	
	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
		condominio = usuario.getCondominio();
		
		blocos = listarTodos();
		if(blocos == null || blocos.isEmpty()){
			quantidadeBlocos = 1;
		}
	}
	
	public boolean mostrarCadastrosBlocos(){
		if(blocos == null || blocos.isEmpty()){
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	
	
	public List<Bloco> listarTodos(){
		return blocoService.recuperarTodos();
	}
	
	public void sugerirBlocos(){
		blocos = new ArrayList<Bloco>();
		for(int i=0; i<quantidadeBlocos; i++){
			Bloco b = new Bloco();
			b.setNome("Bloco "+(i+1));
			blocos.add(b);
		}		
	}
	
	public void onRowEdit(RowEditEvent event) {
        message.addInfo("Bloco editado "+ ((Bloco) event.getObject()).getId());
    }
     
    public void onRowCancel(RowEditEvent event) {
    	message.addInfo("Edição cancelada!");
    }
    
    
    public String onFlowProcess(FlowEvent event) {
        if(true) {
//            skip = false;   //reset in case user goes back
            return "confirm";
        }
        else {
            return event.getNewStep();
        }
    }
     
	

	// GETTERS E SETTERS
	
	public List<Bloco> getBlocos() {
		return blocos;
	}

	public void setBlocos(List<Bloco> blocos) {
		this.blocos = blocos;
	}

	public String getNomeCondominio() {
		return condominio.getNome();
	}

	public Integer getQuantidadeBlocos() {
		return quantidadeBlocos;
	}

	public void setQuantidadeBlocos(Integer quantidadeBlocos) {
		this.quantidadeBlocos = quantidadeBlocos;
	}
		
}
