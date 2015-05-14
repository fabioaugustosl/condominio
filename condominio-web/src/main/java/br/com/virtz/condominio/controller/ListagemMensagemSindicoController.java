package br.com.virtz.condominio.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.primefaces.component.menu.AbstractMenu;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.virtz.condominio.constantes.EnumTipoUsuario;
import br.com.virtz.condominio.entidades.ArquivoAtaAssembleia;
import br.com.virtz.condominio.entidades.Assembleia;
import br.com.virtz.condominio.entidades.MensagemSindico;
import br.com.virtz.condominio.entidades.PautaAssembleia;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exceptions.CondominioException;
import br.com.virtz.condominio.service.IAssembleiaService;
import br.com.virtz.condominio.service.IMensagemSindicoService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.IArquivosUtil;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class ListagemMensagemSindicoController {

	@EJB
	private IMensagemSindicoService mensagemSindicoService;
	
	@Inject
	private MessageHelper messageHelper;
	
	@Inject
	private SessaoUsuario sessao;
	
	
	private List<MensagemSindico> mensagens;
	
	@PostConstruct
	public void init(){
		mensagens = listarTodos(); 
	}
	
	
	public List<MensagemSindico> listarTodos(){
		Usuario usuario = sessao.getUsuarioLogado();
		
		List<MensagemSindico> lista = mensagemSindicoService.recuperarTodos(usuario.getCondominio().getId());
		
		return lista;
	}
	
	
	public void removerMensagem(MensagemSindico msg) throws CondominioException {
		if(msg != null){
			mensagemSindicoService.remover(msg.getId());
			messageHelper.addInfo("Mensagem removida com sucesso!");
			mensagens.remove(msg);
		}
	}

	
	
	// GETTERS E SETTERS
	public List<MensagemSindico> getMensagens() {
		return mensagens;
	}

	public void setMensagens(List<MensagemSindico> mensagens) {
		this.mensagens = mensagens;
	}
	 	 
	 		
}
