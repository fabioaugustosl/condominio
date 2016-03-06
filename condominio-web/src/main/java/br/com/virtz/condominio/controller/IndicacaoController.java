package br.com.virtz.condominio.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import br.com.virtz.condominio.entidades.Indicacao;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.service.IIndicacaoService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.IArquivosUtil;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class IndicacaoController {
	
	@EJB
	private IIndicacaoService indicacaoService;

	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private NavigationPage navegacao;

	@Inject
	private IArquivosUtil arquivoUtil;
	
	
	private Usuario usuario = null; 
	private List<Indicacao> indicacoes = null;
	
	
	@PostConstruct
	public void init(){
		
		usuario = sessao.getUsuarioLogado();
		
		recuperarIndicacoes();
		
	}
	
	
	public void recuperarIndicacoes(){
		indicacoes = indicacaoService.recuperarIndicacoesRecentes(usuario.getCondominio().getId(), 9l);
		
	}
	
	
	public void irParaCadastro(){
		navegacao.redirectToPage("/indicacao/cadastrarIndicacao.faces");
	}
	
	
	public void irParaListagem(){
		navegacao.redirectToPage("/indicacao/listagemIndicacao.faces");
	}
	
	public void ver(Indicacao indicacao){
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("idIndicacaoDestaque", indicacao.getId());
		navegacao.redirectToPage("/indicacao/listagemIndicacao.faces");
	}

	
	/* GETTERS e SETTERS */
	public List<Indicacao> getIndicacoes() {
		return indicacoes;
	}

	public void setIndicacoes(List<Indicacao> indicacoes) {
		this.indicacoes = indicacoes;
	}
	
	
}
