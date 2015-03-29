package br.com.virtz.condominio.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.service.IUsuarioService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@RequestScoped
public class ConfiguracaoController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private NavigationPage navegacao;
	
	@Inject
	private MessageHelper messageHelper;

	@Inject
	private SessaoUsuario sessao;
	
	@EJB
	private IUsuarioService usuarioService;
	

	private Usuario usuario = null;
	private String senhaAtual = null;
	private String senhaNova = null;
	private String senhaNova2 = null;
	
	private boolean sucesso;
	private String msgErro = null;

	@PostConstruct
	public void init() {
		senhaAtual = null;
		senhaNova = null;
		senhaNova2 = null;
		msgErro = null;
		sucesso = false;
		usuario = sessao.getUsuarioLogado();
	}
	
	
	public void alterarSenha(ActionEvent event) throws AppException{
		
		String senhaAtualCript = usuarioService.criptografarSenhaUsuario(senhaAtual);
		if(!senhaAtualCript.equals(usuario.getSenha())){
			msgErro = "Senha atual incorreta!";
			sucesso = false;
			return;
		}
		
		try{
			Usuario u = usuarioService.recuperarUsuarioCompleto(usuario.getId());
			u.setSenhaDigitada(senhaNova);
			usuario = usuarioService.salvar(u);
			sessao.setUsuarioLogado(usuario);
			sucesso = true;
			msgErro = null;
		}catch(Exception e){
			sucesso = false;
			msgErro = "Ocorreu um erro ao alterar a senha.";
		} finally {
			senhaAtual = null;
			senhaNova = null;
			senhaNova2 = null;
		}
			
		//TODO : enviar Email avisando
		
	}
	

	public void irParaPrincipal() {
		navegacao.redirectToPage("/portal.faces");
	}

	public String getSenhaAtual() {
		return senhaAtual;
	}

	public void setSenhaAtual(String senhaAtual) {
		this.senhaAtual = senhaAtual;
	}

	public String getSenhaNova() {
		return senhaNova;
	}

	public void setSenhaNova(String senhaNova) {
		this.senhaNova = senhaNova;
	}

	public String getSenhaNova2() {
		return senhaNova2;
	}

	public void setSenhaNova2(String senhaNova2) {
		this.senhaNova2 = senhaNova2;
	}

	public boolean isSucesso() {
		return sucesso;
	}

	public String getMsgErro() {
		return msgErro;
	}

	
}
