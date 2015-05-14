package br.com.virtz.condominio.controller;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import br.com.virtz.condominio.constantes.EnumTipoUsuario;
import br.com.virtz.condominio.entidades.Avaliacao;
import br.com.virtz.condominio.entidades.BatePapo;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.MensagemSindico;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.ErroAoSalvar;
import br.com.virtz.condominio.service.IMensagemSindicoService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.MessageHelper;

@ManagedBean
@ViewScoped
public class MensagemSindicoController {

	@EJB
	private IMensagemSindicoService mensagemService;
	
	@Inject
	private MessageHelper messageHelper;
	
	@Inject
	private SessaoUsuario sessao;
	
	private String mensagem;
	private Usuario usuario;
	
	
	@PostConstruct
	public void init(){
		mensagem = null;
		usuario = sessao.getUsuarioLogado();
	}
	
	
		
	public void enviarMensagem(ActionEvent actionEvent) {
		if(StringUtils.isNotBlank(this.mensagem)){
			MensagemSindico msg = new MensagemSindico();
			msg.setCondominio(usuario.getCondominio());
			msg.setData(new Date());
			msg.setMensagem(this.mensagem);
			msg.setUsuario(usuario);
			try {
				mensagemService.salvar(msg);
				messageHelper.addInfo("Sua mensagem enviada para o sindico!");
				this.mensagem = null;
				
				// TODO  :  Enviar email
			} catch (Exception e) {
				messageHelper.addError("Ocorreu um erro ao enviar sua mensagem.");
			}
		}
    }


	// GETTERS E SETTERS
	
	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
		
}
