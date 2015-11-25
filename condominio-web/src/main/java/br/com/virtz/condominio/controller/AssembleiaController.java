package br.com.virtz.condominio.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.primefaces.context.RequestContext;

import br.com.virtz.condominio.entidades.Assembleia;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.exception.ErroAoSalvar;
import br.com.virtz.condominio.service.IAssembleiaService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class AssembleiaController {

	@EJB
	private IAssembleiaService assembleiaService;
	
	@Inject
	private MessageHelper messageHelper;
	
	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private NavigationPage navegacao;
	
	
	private List<Assembleia> assembleias;
	private Assembleia assembleiaSelecionada;
	private String textoPauta = null;
	
	private Usuario usuario = null;
	
	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
		assembleias = listarTodos(); 
	}
	
	
	public List<Assembleia> listarTodos(){
		
		List<Assembleia> lista =  assembleiaService.recuperarAssembleiasNaoRealizadas(usuario.getCondominio().getId());
		
		return lista;
	}
	
	
	public void novaPauta() throws AppException{
		if(StringUtils.isNotBlank(textoPauta)){
			try {
				assembleiaService.novaPauta(assembleiaSelecionada.getId(), textoPauta, usuario);
				messageHelper.addInfo("Pauta enviada com sucesso! Aguarde até o sindico aprová-la.");
				textoPauta = null;
				assembleiaSelecionada = null;
			} catch (ErroAoSalvar e) {
				throw new AppException(e.getMessage());
			}
		}
	}
	
	public void resetPauta(){
		textoPauta = null;
	}
	
	public boolean possuiAssembleiaAgendada(){
		if(assembleias == null || assembleias.isEmpty()){
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	
	
	public void verPautas() {
        RequestContext.getCurrentInstance().openDialog("dialogVerPautas");
    }
	
	 
	// GETTERS E SETTERS
	public List<Assembleia> getAssembleias() {
		return assembleias;
	}

	public void setAssembleias(List<Assembleia> assembleias) {
		this.assembleias = assembleias;
	}

	public Assembleia getAssembleiaSelecionada() {
		return assembleiaSelecionada;
	}

	public void setAssembleiaSelecionada(Assembleia assembleiaSelecionada) {
		this.assembleiaSelecionada = assembleiaSelecionada;
	}

	public String getTextoPauta() {
		return textoPauta;
	}

	public void setTextoPauta(String textoPauta) {
		this.textoPauta = textoPauta;
	}
	 
		
}
