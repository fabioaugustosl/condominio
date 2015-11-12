package br.com.virtz.condominio.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import br.com.virtz.condominio.entidades.Balanco;
import br.com.virtz.condominio.entidades.ItemBalanco;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.service.IBalancoService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class DetalharBalancoController {

	
	@EJB
	private IBalancoService balancoService;
	
	@Inject
	private MessageHelper message;
	
	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private NavigationPage navigation;
	
	
	private Usuario usuario = null;
	private Balanco balanco = null;
	
	private List<ItemBalanco> receitas = null;
	private List<ItemBalanco> despesas = null;
	
	
	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
		Object competenciaDetalhar = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("idBalanco");
		
		if(competenciaDetalhar == null){
			receitas = new ArrayList<ItemBalanco>();
			despesas = new ArrayList<ItemBalanco>();
		} else {
			montarBalanco(Long.parseLong(competenciaDetalhar.toString()));
		}
	}
	
	
	public void montarBalanco(Long idBalanco){
		
		balanco = balancoService.recuperarBalanco(idBalanco);
		
		if(balanco != null){
			try {
				despesas = balancoService.recuperarDespesas(balanco);
				receitas = balancoService.recuperarReceitas(balanco);
			} catch (AppException e) {
			}
		} 

	}

	
	public void irParaListagem(){
		navigation.redirectToPage("/balanco/listagemBalanco.faces");
	}

	
	
	/* GETTER E SETTERS*/
	
	public List<ItemBalanco> getReceitas() {
		return receitas;
	}

	public List<ItemBalanco> getDespesas() {
		return despesas;
	}

	public Balanco getBalanco() {
		return balanco;
	}

}
