package br.com.virtz.condominio.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import br.com.virtz.condominio.bean.Email;
import br.com.virtz.condominio.constantes.EnumTemplates;
import br.com.virtz.condominio.email.EnviarEmail;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exceptions.CondominioException;
import br.com.virtz.condominio.service.IUsuarioService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.MessageHelper;

@ManagedBean
@ViewScoped
public class EnviarMensagemUsuarioController {

	@EJB
	private IUsuarioService usuarioService;
	
	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private MessageHelper msgHelper;
	
	
	@EJB
	private EnviarEmail enviarEmail;
	
	
	private List<Usuario> usuarios = null;
	private List<Usuario> usuariosSelecionados = null;
	private String mensagem = null;
	private String assunto = null;
	
	
	
	@PostConstruct
	public void init(){
		usuarios = listarTodos(); 
		usuariosSelecionados = new ArrayList<Usuario>();
	}
	
	
	public List<Usuario> listarTodos(){
		Usuario usuario = sessao.getUsuarioLogado();
		
		List<Usuario> usuarios = usuarioService.recuperarTodos(usuario.getCondominio());
		
		return usuarios;
	}
	
	
	public void enviarEmail(ActionEvent event) throws CondominioException {
		if(StringUtils.isBlank(this.assunto)){
			throw new CondominioException("Você não preencheu o assunto da mensagem.");
		}
		if(StringUtils.isBlank(this.mensagem)){
			throw new CondominioException("Você não preencheu a mensagem a ser enviada.");
		}
		if(usuariosSelecionados == null || usuariosSelecionados.isEmpty()){
			throw new CondominioException("É necessário preencher pelo menos um condômino.");
		}
		
		for(Usuario u : usuariosSelecionados){
			Email email = new Email(EnumTemplates.PADRAO.getDe(), u.getEmail(), this.assunto, this.mensagem);
			enviarEmail.enviar(email);
		}
		
		usuariosSelecionados = new ArrayList<Usuario>();
		this.assunto = null;
		this.mensagem = null;
		msgHelper.addInfo("Sua mensagem foi enviada aos condôminos selecionados!");
	}

	
	public void enviarSms(ActionEvent event) throws CondominioException {
		
	}
	
	
	// GETTERS E SETTERS
	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

	public List<Usuario> getUsuariosSelecionados() {
		return usuariosSelecionados;
	}

	public void setUsuariosSelecionados(List<Usuario> usuariosSelecionados) {
		this.usuariosSelecionados = usuariosSelecionados;
	}
	

}
