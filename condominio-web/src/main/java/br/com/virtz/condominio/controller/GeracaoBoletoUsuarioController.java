package br.com.virtz.condominio.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.model.StreamedContent;

import br.com.virtz.condominio.entidades.CobrancaUsuario;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.service.ICondominioService;
import br.com.virtz.condominio.service.IFinanceiroService;
import br.com.virtz.condominio.service.IUsuarioService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.MessageHelper;

@ManagedBean
@ViewScoped
public class GeracaoBoletoUsuarioController {

	@EJB
	private IUsuarioService usuarioService;
	
	@EJB
	private IFinanceiroService financeiroService;	
	
	@EJB
	private ICondominioService condominioService;
	
	@Inject
	private MessageHelper message;
	
	@Inject
	private SessaoUsuario sessao;
	
	
	@Inject
	private DownloadBoletoController downloadController;
	
	
	private List<CobrancaUsuario> cobrancas = null;
	private Usuario usuario = null;
	
	
		
	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
		cobrancas  = financeiroService.recuperarCobrancasUsuario(usuario.getCondominio().getId(), usuario.getId());
		if(cobrancas == null || cobrancas.isEmpty()){
			message.addWarn("Ainda não foram geradas cobranças para você!");
		}
	}
	
	
	
	public StreamedContent download(CobrancaUsuario cobranca) {   
		return downloadController.download(cobranca);
    }


	
	
	public List<CobrancaUsuario> getCobrancas() {
		return cobrancas;
	}

	public void setCobrancas(List<CobrancaUsuario> cobrancas) {
		this.cobrancas = cobrancas;
	}

		
}
