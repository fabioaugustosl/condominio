package br.com.virtz.condominio.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import br.com.virtz.condominio.constantes.EnumTaxaCreditoDebito;
import br.com.virtz.condominio.constantes.EnumTaxaPorcentagemValor;
import br.com.virtz.condominio.entidades.Taxa;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exceptions.CondominioException;
import br.com.virtz.condominio.service.ICondominioService;
import br.com.virtz.condominio.service.IFinanceiroService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class CadastroTaxaController {
	
	@EJB
	private ICondominioService condominioService;
	
	@EJB
	private IFinanceiroService financeiroService;
	

	
	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private MessageHelper message;
	
	@Inject
	private NavigationPage navigation;
	
	
	private Usuario usuario = null;
	
	private Taxa taxa = null;
	
	private String tipo = null;  
	private Map<String,String> tipos = new HashMap<String, String>();
	private String creditoDebito = null;
	private Map<String,String> creditoDebitos = new HashMap<String, String>();
	
	
	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
				
		for(EnumTaxaPorcentagemValor b : EnumTaxaPorcentagemValor.values()){
			tipos.put( b.getDescricao(), b.toString());
		}
		
		for(EnumTaxaCreditoDebito b : EnumTaxaCreditoDebito.values()){
			creditoDebitos.put( b.getDescricao(), b.toString());
		}
		
		Object taxaEditar = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("idTaxa");
		if(taxaEditar != null){
			taxa = financeiroService.recuperarTaxa(Long.parseLong(taxaEditar.toString()));
			this.tipo = taxa.getPorcentagemOuValor().toString();
			this.creditoDebito = taxa.getCreditoOuDebito().toString();
		} else {
			taxa = new Taxa();
			taxa.setCondominio(usuario.getCondominio());
		}
		 
	}
	
	
	public void salvar(ActionEvent event) throws CondominioException {
		try{
			EnumTaxaPorcentagemValor enumTipo = EnumTaxaPorcentagemValor.recuperarPorDescricao(this.tipo);
			EnumTaxaCreditoDebito enumCreditoDebito = EnumTaxaCreditoDebito.recuperarPorDescricao(creditoDebito);
			
			taxa.setCreditoOuDebito(enumCreditoDebito);
			taxa.setPorcentagemOuValor(enumTipo);
			
			financeiroService.salvar(taxa);
			
			taxa = null;
			this.tipo = null;
			this.creditoDebito = null;
			
			message.addInfo("A taxa foi salva com sucesso.");

		}catch(Exception e){
			e.printStackTrace();
			throw new CondominioException("Ocorreu um erro inesperado ao salvar a taxa.");
		}
	}
	
	public void voltar(){
		navigation.redirectToPage("/financeiro/gerenciarFinanceiro.faces");
	}
	
	public void irParaListagem(){
		navigation.redirectToPage("/financeiro/listagemTaxas.faces");
	}
	
	/* GETTERS e SETTERS*/
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Taxa getTaxa() {
		return taxa;
	}

	public void setTaxa(Taxa taxa) {
		this.taxa = taxa;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Map<String, String> getTipos() {
		return tipos;
	}

	public void setTipos(Map<String, String> tipos) {
		this.tipos = tipos;
	}

	public String getCreditoDebito() {
		return creditoDebito;
	}

	public void setCreditoDebito(String creditoDebito) {
		this.creditoDebito = creditoDebito;
	}

	public Map<String, String> getCreditoDebitos() {
		return creditoDebitos;
	}

	public void setCreditoDebitos(Map<String, String> creditoDebitos) {
		this.creditoDebitos = creditoDebitos;
	}
		
	
}
