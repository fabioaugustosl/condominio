package br.com.virtz.condominio.controller;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import br.com.virtz.condominio.constantes.EnumParametroSistema;
import br.com.virtz.condominio.entidades.AcessoCFTV;
import br.com.virtz.condominio.entidades.ParametroSistema;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.service.ICondominioService;
import br.com.virtz.condominio.service.IParametroSistemaService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.MessageHelper;

@ManagedBean
@ViewScoped
public class ParametroController {
	
	@EJB
	private IParametroSistemaService parametroService;
	
	@EJB
	private ICondominioService condominioService;
	
	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private PrincipalController principalController;
	
	@Inject
	private MessageHelper mensagem;
	
	private Usuario usuario = null;
	
	private ParametroSistema parametroMaximoDias = null;
	private ParametroSistema parametroEnviarEmailAta = null;
	private Boolean parametroEnviarEmailAtaBool = null;
	
	private AcessoCFTV cftv = null;
	
	
	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
		parametroMaximoDias = parametroService.recuperar(EnumParametroSistema.QUANTIDADE_DIAS_MAXIMO_PARA_AGENDAR_AREA_COMUM, usuario.getCondominio());
		parametroEnviarEmailAta = parametroService.recuperar(EnumParametroSistema.AVISAR_POR_EMAIL_QUANDO_AXEXAR_ATA, usuario.getCondominio());
		parametroEnviarEmailAtaBool = ("1".equals(parametroEnviarEmailAta.getValor()) ? Boolean.TRUE: Boolean.FALSE);
		
		// cftv
		cftv = condominioService.recuperarCFTV(usuario.getCondominio().getId());
		if(cftv == null){
			cftv = new AcessoCFTV();
		}
		
	}
	
	
	public void salvar() throws AppException{
		
		try {
			parametroService.salvar(parametroMaximoDias);
			
			parametroEnviarEmailAta.setValor(parametroEnviarEmailAtaBool ? "1" : "0");
			parametroService.salvar(parametroEnviarEmailAta);
			
			mensagem.addInfo("Configurações atualizadas com sucesso.");
		} catch (Exception e) {
			throw new AppException("Ocorreu um erro ao salvar o(s) bloco(s). Favor acesse o menu novamente e repita o processo.");
		}

	}

	
	public void salvarCFTV() throws AppException{
		
		try {
			if(cftv.getCondominio() == null){
				cftv.setCondominio(usuario.getCondominio());
			}
			
			cftv = condominioService.salvarAcessoCFTV(cftv);
			
			// atualiza variavel da sessão
			principalController.getCondominio().setCftv(cftv);
			
			mensagem.addInfo("Configurações de acesso a sua CFTV foram atualizadas com sucesso.");
		} catch (Exception e) {
			throw new AppException("Ocorreu um erro ao salvar dados de acesso a sua CFTV.");
		}

	}
	
	

	// GETTERS E SETTERS
	public ParametroSistema getParametroMaximoDias() {
		return parametroMaximoDias;
	}

	public void setParametroMaximoDias(ParametroSistema parametroMaximoDias) {
		this.parametroMaximoDias = parametroMaximoDias;
	}

	public Boolean getParametroEnviarEmailAtaBool() {
		return parametroEnviarEmailAtaBool;
	}

	public void setParametroEnviarEmailAtaBool(Boolean parametroEnviarEmailAtaBool) {
		this.parametroEnviarEmailAtaBool = parametroEnviarEmailAtaBool;
	}


	public AcessoCFTV getCftv() {
		return cftv;
	}

	public void setCftv(AcessoCFTV cftv) {
		this.cftv = cftv;
	}
	
		    
}
