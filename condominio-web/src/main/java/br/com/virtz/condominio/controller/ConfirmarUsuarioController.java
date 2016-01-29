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
import br.com.virtz.condominio.service.ICondominioService;
import br.com.virtz.condominio.service.ITokenService;
import br.com.virtz.condominio.service.IUsuarioService;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class ConfirmarUsuarioController {
	
	@EJB
	private IUsuarioService usuarioService;
	
	@EJB
	private ICondominioService condominioService;
	
	@EJB
	private ITokenService tokenService;
	
	@Inject
	private NavigationPage navegacao;
	
	@Inject
	private EnviarEmailSuporteController emailSuporte;
	

	private boolean usuarioConfirmado;
	private Usuario usuario = null;
		
	
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
				usuario = usuarioService.recuperarUsuarioCompleto(Long.valueOf(chave));
				
				usuario.setCadastroConfirmado(Boolean.TRUE);
				try {
					usuario = usuarioService.salvar(usuario);
				} catch (Exception e) {
					e.printStackTrace();
					try{
						emailSuporte.enviarEmail("Ocorreu um erro inesperado confirmar o usuário.", e.getMessage(), null);
					}catch(Exception e1){
					}
				}
			}
			
			tokenService.invalidar(token);
			usuarioConfirmado = Boolean.TRUE;
		} else {
			usuarioConfirmado = Boolean.FALSE;
		}
				
	}
	
	
	public void selecionarCondominio(){
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("idUsuario", usuario.getId());
		navegacao.redirectToPage("selecionarCondominioUsuario.faces");
	}

	
	public void irParaLogin(){
		navegacao.redirectToPage("../login.faces");
	}
	
	
	/* GETTERS e SETTERS*/
	
	public boolean isUsuarioConfirmado() {
		return usuarioConfirmado;
	}

	public void setUsuarioConfirmado(boolean usuarioConfirmado) {
		this.usuarioConfirmado = usuarioConfirmado;
	}

	
}
