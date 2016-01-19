package br.com.virtz.condominio.controller;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import br.com.virtz.condominio.constantes.EnumParametroSistema;
import br.com.virtz.condominio.entidades.ParametroSistema;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.service.IParametroSistemaService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.MessageHelper;

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
