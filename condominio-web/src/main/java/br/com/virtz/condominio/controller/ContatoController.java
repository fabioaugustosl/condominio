package br.com.virtz.condominio.controller;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import br.com.virtz.condominio.bean.Email;
import br.com.virtz.condominio.constantes.EnumTemplates;
import br.com.virtz.condominio.email.EnviarEmail;
import br.com.virtz.condominio.exceptions.CondominioException;
import br.com.virtz.condominio.util.MessageHelper;

@ManagedBean
@ViewScoped
public class ContatoController {

	@Inject
	private MessageHelper msgHelper;
	
	@EJB
	private EnviarEmail enviarEmail;
	
	
	private String mensagem = null;
	private String email = null;
	private String nome = null;
	
	
	@PostConstruct
	public void init(){
	}
	
	
	public void enviarEmail(ActionEvent event) throws CondominioException {
		StringBuilder msg = new StringBuilder();
		msg.append("Nome : ").append(this.nome).append("<br >");
		msg.append("Email : ").append(this.email).append("<br >");
		msg.append("Mensagem : ").append("<br >").append(this.mensagem).append("<br >");
			
		Email emailEnviar = new Email(EnumTemplates.PADRAO.getDe(), EnumTemplates.PADRAO.getDe(), "Contato pelo site - Condominio SOBControle", msg.toString());
		enviarEmail.enviar(emailEnviar);
			
		this.mensagem = null;
		this.email = null;
		this.nome = null;
		
		msgHelper.addInfo("Obrigado. Sua mensagem foi enviada com sucesso!");
	}


	public String getMensagem() {
		return mensagem;
	}


	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}

}
