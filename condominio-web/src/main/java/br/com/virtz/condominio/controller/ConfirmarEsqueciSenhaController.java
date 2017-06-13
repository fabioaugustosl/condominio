package br.com.virtz.condominio.controller;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import br.com.virtz.condominio.entidades.Token;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.exceptions.CondominioException;
import br.com.virtz.condominio.service.ITokenService;
import br.com.virtz.condominio.service.IUsuarioService;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class ConfirmarEsqueciSenhaController {
	
	
	@EJB
	private IUsuarioService usuarioService;
	
	@EJB
	private ITokenService tokenService;
	
	@Inject
	private NavigationPage navegacao;
	
	@Inject
	private MessageHelper messageHelper;
	

	private boolean emailConfirmado = false;
	private boolean senhaAtualizada = false;
	
	private Usuario usuario = null;
	
	private String token = null;
	
	
	
	
	

	@PostConstruct
	public void init(){
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest(); 
 
		token = request.getParameter("token");
		
		// verifica se o token é valido
		if(tokenService.tokenEhValido(token)){
			Token tokenEntidade = tokenService.recuperarToken(token);
			
			// recuperar o id do usuario para busca-lo no banco
			String chave = tokenEntidade.getChaveEntidade();
			if(StringUtils.isNotBlank(chave)){
				usuario = usuarioService.recuperarUsuarioCompleto(Long.valueOf(chave));
			}
			emailConfirmado = Boolean.TRUE;
		} else {
			emailConfirmado = Boolean.FALSE;
		}
				
	}
	
	
	public void salvar() throws CondominioException{

		if(usuario.getSenhaDigitada().length() < 6){
			messageHelper.addError("A senha deve possuir no mínimo 6 caracteres.");
			return;
		}
		
		tokenService.invalidar(token);
		try {
			usuarioService.salvar(usuario);
			messageHelper.addInfo("Senha atualizada com sucesso!");
			senhaAtualizada = true;
		} catch (AppException e) {
			throw new CondominioException("Ocorreu um erro ao atualizar a senha do usuário. Favor tentar novamente.");
		}
	}
	
	
	public void irParaLogin(){
		navegacao.redirectToPage("/login.faces");
	}



	
	/* GETTERS e SETTERS*/

	public boolean isEmailConfirmado() {
		return emailConfirmado;
	}

	public void setEmailConfirmado(boolean emailConfirmado) {
		this.emailConfirmado = emailConfirmado;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public boolean isSenhaAtualizada() {
		return senhaAtualizada;
	}

	public void setSenhaAtualizada(boolean senhaAtualizada) {
		this.senhaAtualizada = senhaAtualizada;
	}

}
