package br.com.virtz.condominio.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import br.com.virtz.condominio.bean.Email;
import br.com.virtz.condominio.constantes.EnumTemplates;
import br.com.virtz.condominio.constantes.EnumTipoUsuario;
import br.com.virtz.condominio.email.template.LeitorTemplate;
import br.com.virtz.condominio.entidades.Token;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.service.ITokenService;
import br.com.virtz.condominio.service.IUsuarioService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.IArquivosUtil;
import br.com.virtz.condominio.util.MessageHelper;

@ManagedBean
@ViewScoped
public class CadastrarUsuarioController {
	
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
	
	
	
	private Usuario usuario;
	
	@PostConstruct
	public void init(){
		usuario = new Usuario();
		usuario.setCadastroConfirmado(Boolean.FALSE);
	}


   
	public void salvar(ActionEvent actionEvent) throws AppException {
        salvar();
    }


	private void salvar() throws AppException {
		usuario.setDataCadastro(new Date());
		usuario.setTipoUsuario(EnumTipoUsuario.MORADOR);
		
        try {
        	usuario = usuarioService.salvar(usuario);
        	
        	// gerar token
        	Token token = tokenService.novoToken(String.valueOf(usuario.getId())); 
        	
        	// enviar email confirmação
        	enviarEmailConfirmacaoCadastro(token);
		} catch (Exception e) {
			throw new AppException("Ocorreu um erro ao salvar seu usuário. Favor tentar novamente.");
		}
	}



	private void enviarEmailConfirmacaoCadastro(Token token) {
		Map<Object, Object> mapParametrosEmail = new HashMap<Object, Object>();
		mapParametrosEmail.put("nomeUsuairo", usuario.getNome());
		mapParametrosEmail.put("token", token.getToken());
		
		String caminho = arquivoUtil.getCaminhaPastaTemplatesEmail();
		String msg = leitor.processarTemplate(caminho, EnumTemplates.CONFIRMACAO_USUARIO.getNomeArquivo(), mapParametrosEmail);
		Email email = new Email("fabioaugustosl@gmail.com", "fabioaugustosl@gmail.com", "teste Fabio", msg);
//    		enviarEmail.enviar(email);
	}



	
	/*  GETTERS e SETTERs	 */

	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	
}
