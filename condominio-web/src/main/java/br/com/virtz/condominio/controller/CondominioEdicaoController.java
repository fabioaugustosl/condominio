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
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.service.IBlocoService;
import br.com.virtz.condominio.service.ICondominioService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class CondominioEdicaoController {

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
	
	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
		condominio = usuario.getCondominio();
		
		blocos = listarTodos();
	}
	
	public List<Bloco> listarTodos(){
		return blocoService.recuperarTodos();
	}
	
	public void salvarBlocos() throws AppException{
		for(Bloco bloco : blocos){
			try {
				blocoService.salvar(bloco);
			} catch (Exception e) {
				throw new AppException("Ocorreu um erro ao salvar o(s) bloco(s). Favor acesse o menu novamente e repita o processo.");
			}
		}
		message.addInfo("Os dados do seu condomínio foram atualizados com sucesso. ");
	}
	
	public void onRowEdit(RowEditEvent event) {
        message.addInfo("Bloco editado "+ ((Bloco) event.getObject()).getId());
    }
     
    public void onRowCancel(RowEditEvent event) {
    	message.addInfo("Edição cancelada!");
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

}
