package br.com.virtz.condominio.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.event.RowEditEvent;

import br.com.virtz.condominio.entidades.Bloco;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.service.ICondominioService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class CondominioController {
	
	@EJB
	private ICondominioService condominioService;
	
	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private MessageHelper message;
	
	@Inject
	private NavigationPage navegacao;
	
	private List<Bloco> blocos;
	private Condominio condominio;
	private Usuario usuario;
	private Integer quantidadeBlocos = null;
	
	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
		condominio = usuario.getCondominio();
		
		blocos = listarTodosBlcoso();
		if(blocos != null && blocos.size() > 0){
			//NavigationPage.redirectToPage("/condominio/condominioEdicao.faces");
			try {
				editarCondominio();
			} catch (Exception e) {
				
			}
		}
		
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
	
	
	public List<Bloco> listarTodosBlcoso(){
		return condominioService.recuperarTodosBlocos();
	}
	
	public void sugerirBlocos(){
		blocos = condominioService.sugerirBlocos(quantidadeBlocos, condominio);		
	}
	

	public void salvarBlocos() throws Exception{
		for(Bloco bloco : blocos){
			try {
				condominioService.salvarBloco(bloco);
			} catch (Exception e) {
				throw new Exception("Ocorreu um erro ao salvar o(s) bloco(s). Favor acesse o menu novamente e repita o processo.");
			}
		}
	}
	
	public void editarCondominio() throws Exception{
		navegacao.redirectToPage("/condominio/condominioEdicao.faces");
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

	public Integer getQuantidadeBlocos() {
		return quantidadeBlocos;
	}

	public void setQuantidadeBlocos(Integer quantidadeBlocos) {
		this.quantidadeBlocos = quantidadeBlocos;
	}
		
}
