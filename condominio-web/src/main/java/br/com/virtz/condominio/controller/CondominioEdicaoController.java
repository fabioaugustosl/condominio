package br.com.virtz.condominio.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.event.RowEditEvent;

import br.com.virtz.condominio.entidades.AreaComum;
import br.com.virtz.condominio.entidades.Bloco;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.service.ICondominioService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.MessageHelper;

@ManagedBean
@ViewScoped
public class CondominioEdicaoController {
	
	@EJB
	private ICondominioService condominioService;
	
	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private MessageHelper message;
	
	private List<Bloco> blocos;
	private List<AreaComum> areas;
	private AreaComum area;
	private Condominio condominio;
	private Usuario usuario;
	private boolean editavel;
	
	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
		condominio = usuario.getCondominio();

		editavel = usuario.isSindico();
		
		blocos = listarTodosBlocos();
		areas = new ArrayList<AreaComum>(condominio.getAreasComuns());
	}
	
	public List<Bloco> listarTodosBlocos(){
		return condominioService.recuperarTodosBlocos();
	}
	
	public void salvar() throws Exception{
		
		try {
			condominioService.salvar(condominio);
		
			for(Bloco bloco : blocos){
				condominioService.salvarBloco(bloco);
			}
		} catch (Exception e) {
			throw new Exception("Ocorreu um erro ao salvar o(s) bloco(s). Favor acesse o menu novamente e repita o processo.");
		}

		message.addInfo("Os dados do seu condomínio foram atualizados com sucesso. ");
	}
	
	
	public void salvarArea() throws Exception{
		
		try {
			area.setCondominio(condominio);
			area = condominioService.salvarAreaComum(area);
			if(areas == null){
				areas = new ArrayList<AreaComum>();
			}
			areas.add(area);
		} catch (Exception e) {
			throw new Exception("Ocorreu um erro ao salvar a(s) áreas(s). Favor acesse o menu novamente e repita o processo.");
		} finally {
			area = null;
		}

		message.addInfo("As áreas comuns de seu condomínio foram atualizadas.");
	}

	public void onRowEdit(RowEditEvent event) {
        message.addInfo("Bloco editado "+ ((Bloco) event.getObject()).getId());
    }
     
    public void onRowCancel(RowEditEvent event) {
    	message.addInfo("Edição cancelada!");
    }
    
    public void excluirArea(AreaComum area){
    	condominioService.removerAreaComum(area.getId());
    	
    	message.addInfo("A área comum "+area.getNome()+" foi excluída com sucesso.");
    }
    
    public void novaArea(){
    	area = new AreaComum();
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

	public boolean isEditavel() {
		return editavel;
	}

	public void setEditavel(boolean editavel) {
		this.editavel = editavel;
	}

	public Condominio getCondominio() {
		return condominio;
	}

	public void setCondominio(Condominio condominio) {
		this.condominio = condominio;
	}

	public List<AreaComum> getAreas() {
		return areas;
	}

	public void setAreas(List<AreaComum> areas) {
		this.areas = areas;
	}

	public AreaComum getArea() {
		return area;
	}

	public void setArea(AreaComum area) {
		this.area = area;
	}
	
	

}
