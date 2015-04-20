package br.com.virtz.condominio.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.primefaces.event.RowEditEvent;

import br.com.virtz.condominio.constantes.EnumParametroSistema;
import br.com.virtz.condominio.entidades.Bloco;
import br.com.virtz.condominio.entidades.Cidade;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.Estado;
import br.com.virtz.condominio.entidades.ParametroSistema;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.service.ICondominioService;
import br.com.virtz.condominio.service.ILocalidadeService;
import br.com.virtz.condominio.service.IParametroSistemaService;
import br.com.virtz.condominio.service.IUsuarioService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class ParametroController {
	
	@EJB
	private IParametroSistemaService parametroService;
	
	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private MessageHelper mensagem;
	
	private ParametroSistema parametroMaximoDias = null;
	
	
	@PostConstruct
	public void init(){
		Usuario usuario = sessao.getUsuarioLogado();
		parametroMaximoDias = parametroService.recuperar(EnumParametroSistema.QUANTIDADE_DIAS_MAXIMO_PARA_AGENDAR_AREA_COMUM, usuario.getCondominio());
	}
	
	
	public void salvar() throws AppException{
		
		try {
			parametroService.salvar(parametroMaximoDias);
			mensagem.addInfo("Configurações atualizadas com sucesso.");
		} catch (Exception e) {
			throw new AppException("Ocorreu um erro ao salvar o(s) bloco(s). Favor acesse o menu novamente e repita o processo.");
		}

	}

	
	

	// GETTERS E SETTERS
	public ParametroSistema getParametroMaximoDias() {
		return parametroMaximoDias;
	}

	public void setParametroMaximoDias(ParametroSistema parametroMaximoDias) {
		this.parametroMaximoDias = parametroMaximoDias;
	}
	
		    
}
