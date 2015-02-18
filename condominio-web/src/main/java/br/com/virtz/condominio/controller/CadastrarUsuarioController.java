package br.com.virtz.condominio.controller;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import org.primefaces.model.CroppedImage;

import br.com.virtz.condominio.constantes.EnumTipoUsuario;
import br.com.virtz.condominio.email.template.LeitorTemplate;
import br.com.virtz.condominio.entidades.ArquivoUsuario;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.service.ITokenService;
import br.com.virtz.condominio.service.IUsuarioService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.IArquivosUtil;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class CadastrarUsuarioController implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@EJB
	private IUsuarioService usuarioService;
	
	@EJB
	private ITokenService tokenService;
	
	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private MessageHelper message;
	
	@Inject
	private IArquivosUtil arquivoUtil;

	@Inject
	private LeitorTemplate leitor;
	
	@Inject
	private NavigationPage navegacao;
	
	
	private CroppedImage imagemCortada = null;
    private String caminhoImagem = null;
    
	private Usuario usuario = null;
	
	@PostConstruct
	public void init(){
		usuario = new Usuario();
		usuario.setArquivo(new ArquivoUsuario());
		usuario.setCadastroConfirmado(Boolean.FALSE);
	}


	private void salvar() throws AppException {
		try {
			usuario.setArquivo(null);
        	usuario = usuarioService.salvarNovo(usuario);
		} catch (AppException app){
			throw app;
		} catch (Exception e) {
			throw new AppException("Ocorreu um erro ao salvar seu usuário. Favor tentar novamente.");
		}
	}

	 
	 public void proximoPasso() throws AppException{
	 	//salvar();
	 	FacesContext.getCurrentInstance().getExternalContext().getFlash().put("idUsuario", 29);
		navegacao.redirectToPage("/usuario/cadastrarUsuarioFoto.faces");
	 }

	
	
	 /*  GETTERS e SETTERs	 */

	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	
}
