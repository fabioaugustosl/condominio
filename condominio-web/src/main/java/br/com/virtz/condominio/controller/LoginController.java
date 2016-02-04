package br.com.virtz.condominio.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import br.com.virtz.condominio.bean.Email;
import br.com.virtz.condominio.constantes.EnumTemplates;
import br.com.virtz.condominio.constantes.EnumTipoUsuario;
import br.com.virtz.condominio.email.EnviarEmail;
import br.com.virtz.condominio.email.template.LeitorTemplate;
import br.com.virtz.condominio.entidades.AreaComum;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.Token;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.geral.ParametroSistemaLookup;
import br.com.virtz.condominio.service.ICondominioService;
import br.com.virtz.condominio.service.ITokenService;
import br.com.virtz.condominio.service.IUsuarioService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.ArquivosUtil;
import br.com.virtz.condominio.util.IArquivosUtil;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class LoginController {
	
	@Inject
	private NavigationPage navigation;
	
	@EJB
	private IUsuarioService usuarioService;
	
	@EJB
	private ICondominioService condominioService;
	
	@EJB
	private ITokenService tokenService;
	
	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private MessageHelper messageHelper;
	
	@Inject
	private ParametroSistemaLookup parametroLookup;
	
	@Inject
	private IArquivosUtil arquivoUtil;

	
	
	private String login;
	private String senha;
	private String emailEsqueciMinhaSenha = null; 
	
	@Inject
	private LeitorTemplate leitor;
	
	@Inject
	private ArquivosUtil arquivosUtil;
	
	@EJB
	private EnviarEmail enviarEmail;
	
	
	public void logar() throws Exception{
		if(StringUtils.isBlank(senha)){
			messageHelper.addError("Favor preencher sua senha de acesso.");
			return;
		}
		//metodoAuxiliarCriacaoUsuarioDesenv();
		UsernamePasswordToken tokenShiro = new UsernamePasswordToken(login, usuarioService.criptografarSenhaUsuario(senha));
		Subject usuarioAtual = SecurityUtils.getSubject();
		try{
			usuarioAtual.login(tokenShiro);
			
			Usuario u = usuarioService.recuperarUsuario(login);
			sessao.setUsuarioLogado(u);
			
			// iniciar lookups
			parametroLookup.iniciarLookup(u.getCondominio());

			navigation.redirectToPage("/portal.faces");
		} catch (AuthenticationException ae) {
			ae.printStackTrace();
			ae.getMessage();
			messageHelper.addError(ae.getMessage());
		}
	}
	
	
	public void novoUsuario(){
		navigation.redirectToPage("/usuario/cadastrarUsuario.faces");
	}
	
	
	/**
	 * Procedimento:
	 *  0 - Verifica se o email existe
	 *  1 - Gerar um token
	 *  2 - Enviar email para email digitado
	 *  3 - Quando o sujeito clicar no link ele poderá digitar uma nova senha
	 * @throws Exception
	 */
	public void esqueciMinhaSenha() throws AppException{
		if(StringUtils.isNotBlank(login)){
			
			Usuario usuario = usuarioService.recuperarUsuario(login);
			if(usuario == null ){
				messageHelper.addError("O email digitado não existe em nossa base de dados.");
				return;
			}
			
			Token token = tokenService.novoToken(usuario.getId().toString());
			
			// TODO - terminar envio de email
			enviarEmailEsqueciMinhaSenha(token, login);
			
			messageHelper.addInfo("Aguarde alguns minutos, acesse seu email para concluir a recuperação de senha.");
		} else {
			messageHelper.addError("Para recuperar sua senha digite seu email e cliente em Equeci Minha Senha.");
		}
	}
	
	
	private void enviarEmailEsqueciMinhaSenha(Token token, String para) {
		// recuperar url da aplicação
		HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		StringBuffer sb = origRequest.getRequestURL().delete(origRequest.getRequestURL().indexOf("login.faces"), origRequest.getRequestURL().toString().length()); 
		sb.append("usuario/confirmarEsqueciSenha.faces?token=").append(token.getToken());
		
		Map<Object, Object> mapParametrosEmail = new HashMap<Object, Object>();
		mapParametrosEmail.put("token", sb.toString());
		
		String caminho = arquivoUtil.getCaminhaPastaTemplatesEmail();
		String msg = leitor.processarTemplate(caminho, EnumTemplates.ESQUECI_MINHA_SENHA.getNomeArquivo(), mapParametrosEmail);
		Email email = new Email(EnumTemplates.ESQUECI_MINHA_SENHA.getDe(), para, EnumTemplates.ESQUECI_MINHA_SENHA.getAssunto(), msg);
   		enviarEmail.enviar(email);
	}
	
	
	public void sair(){
		Subject currentUser = SecurityUtils.getSubject();
		currentUser.logout();
	}


	private void metodoAuxiliarCriacaoUsuarioDesenv() throws Exception {
		Usuario u = new Usuario();
		u.setEmail("fabioaugustosl@gmail.com");
		u.setNome("Fabio");
		
		if(StringUtils.isNotBlank(login) && login.equalsIgnoreCase("sindico") ){
			u.setTipoUsuario(EnumTipoUsuario.SINDICO);
		} else {
			u.setTipoUsuario(EnumTipoUsuario.MORADOR);
		}
		
		Condominio c = new Condominio();
		c.setNome("Ponto Imperial");
		
		c = condominioService.salvar(c);
		
		AreaComum ac = new AreaComum();
		ac.setNome("Churrasqueira");
		ac.setCondominio(c);
		
		ac = condominioService.salvarAreaComum(ac);
		
		c.setAreasComuns(new HashSet<AreaComum>());
		c.getAreasComuns().add(ac);
		
		u.setCondominio(c);
		
		u = usuarioService.salvar(u);
	}



	
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getEmailEsqueciMinhaSenha() {
		return emailEsqueciMinhaSenha;
	}

	public void setEmailEsqueciMinhaSenha(String emailEsqueciMinhaSenha) {
		this.emailEsqueciMinhaSenha = emailEsqueciMinhaSenha;
	}
	
	
		
}
