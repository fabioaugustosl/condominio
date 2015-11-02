package br.com.virtz.condominio.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import br.com.virtz.condominio.entidades.Taxa;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.exceptions.CondominioException;
import br.com.virtz.condominio.service.IFinanceiroService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class ListagemTaxaController {
	
	@EJB
	private IFinanceiroService financeiroService;
	
	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private MessageHelper message;
	
	@Inject
	private NavigationPage navigation;
	
	
	private List<Taxa> taxas;
	private Usuario usuario = null;
	
	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
		taxas = financeiroService.recuperarTaxaPorCondominio(usuario.getCondominio().getId());
	}
	
	
	public void excluirTaxa(Taxa taxa) throws CondominioException{
		try {
			financeiroService.removerTaxa(taxa.getId());
		} catch (AppException e) {
			throw new CondominioException(e.getMessage());
		}
		message.addInfo("A taxa foi excluída!");
	}
	
	
	public void voltar(){
		navigation.redirectToPage("/financeiro/gerenciarFinanceiro.faces");
	}
	
	public void irParaCadastro(){
		navigation.redirectToPage("/financeiro/cadastrarTaxas.faces");
	}
	
	public void editar(Taxa taxa){
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("idTaxa", taxa.getId());
		navigation.redirectToPage("/financeiro/cadastrarTaxas.faces");
	}
	
	
	
	/* GETTERS E SETTERS*/

	public List<Taxa> getTaxas() {
		return taxas;
	}

	public void setTaxas(List<Taxa> taxas) {
		this.taxas = taxas;
	}

}

