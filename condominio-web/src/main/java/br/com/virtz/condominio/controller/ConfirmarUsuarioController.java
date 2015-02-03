package br.com.virtz.condominio.controller;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import br.com.virtz.condominio.entidades.Token;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.service.ITokenService;
import br.com.virtz.condominio.service.IUsuarioService;

@ManagedBean
@ViewScoped
public class ConfirmarUsuarioController {
	
	@EJB
	private IUsuarioService usuarioService;
	
	@EJB
	private ITokenService tokenService;
	

	private boolean usuarioConfirmado;
	private boolean condominioExistente = Boolean.FALSE;
	
	
	@PostConstruct
	public void init(){
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest(); 
 
		String token = request.getParameter("token");
		
		// verifica se o token é valido
		if(tokenService.tokenEhValido(token)){
			Token tokenEntidade = tokenService.recuperarToken(token);
			
			// recuperar o id do usuario para busca-lo no banco
			String chave = tokenEntidade.getChaveEntidade();
			if(StringUtils.isNotBlank(chave)){
				Usuario usuario = usuarioService.recuperarUsuarioCompleto(Long.valueOf(chave));
				
				// se o usuário for o primeiro usuário do condominio
				if(usuario.getCondominio() != null){
					condominioExistente = Boolean.TRUE;
				}
				usuario.setCadastroConfirmado(Boolean.TRUE);
				try {
					usuarioService.salvar(usuario);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			tokenService.invalidar(token);
			usuarioConfirmado = Boolean.TRUE;
		} else {
			usuarioConfirmado = Boolean.FALSE;
		}
				
	}

	
	
	/* GETTERS e SETTERS*/
	
	public boolean isUsuarioConfirmado() {
		return usuarioConfirmado;
	}

	public void setUsuarioConfirmado(boolean usuarioConfirmado) {
		this.usuarioConfirmado = usuarioConfirmado;
	}

	public boolean isCondominioExistente() {
		return condominioExistente;
	}

	public void setCondominioExistente(boolean condominioExistente) {
		this.condominioExistente = condominioExistente;
	}
	

}
