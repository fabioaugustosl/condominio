package br.com.virtz.condominio.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import br.com.virtz.condominio.bean.Email;
import br.com.virtz.condominio.constantes.EnumFuncaoBloqueio;
import br.com.virtz.condominio.constantes.EnumTemplates;
import br.com.virtz.condominio.constantes.EnumTipoUsuario;
import br.com.virtz.condominio.email.EnviarEmail;
import br.com.virtz.condominio.email.template.LeitorTemplate;
import br.com.virtz.condominio.entidades.BloqueioFuncaoUsuario;
import br.com.virtz.condominio.entidades.Token;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.exceptions.CondominioException;
import br.com.virtz.condominio.service.ITokenService;
import br.com.virtz.condominio.service.IUsuarioService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.IArquivosUtil;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class ListagemUsuarioController {

	@EJB
	private IUsuarioService usuarioService;

	@Inject
	private MessageHelper messageHelper;

	@EJB
	private ITokenService tokenService;

	@Inject
	private SessaoUsuario sessao;

	@Inject
	private NavigationPage navegacao;

	@Inject
	private IArquivosUtil arquivoUtil;

	@Inject
	private LeitorTemplate leitor;

	@EJB
	private EnviarEmail enviarEmail;

	private List<Usuario> usuarios;
	private List<Usuario> usuariosFiltrados;
	private Usuario usuarioSelecionado = null;
	private BloqueioFuncaoUsuario bloqueioUsuarioSelecionado = null;
	private List<BloqueioFuncaoUsuario> usuariosBloqueados = null;

	private Usuario usuario = null;

	private Usuario usuarioConvite = null;

	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();

		usuarios = listarTodos();
		usuariosFiltrados = new ArrayList<Usuario>();
		usuarioConvite = new Usuario();
		usuariosBloqueados = usuarioService.recuperarBloqueiosPorCondominio(usuario.getCondominio().getId(), EnumFuncaoBloqueio.RESERVA);
	}


	public List<Usuario> listarTodos(){

		List<Usuario> usuarios = usuarioService.recuperarTodos(usuario.getCondominio());

		return usuarios;
	}


	public void remover(Usuario usuario) throws CondominioException {
		usuarioService.remover(usuario.getId());
		usuarios.remove(usuario);
		messageHelper.addInfo("Usuário removido com sucesso!");
	}


	public void enviarConvite() throws AppException {
		try {

				if(validarObrigatoriedadeCamposConvite()){
					return;
				}

				// validar se o email já existe
				Usuario u = usuarioService.recuperarUsuario(usuarioConvite.getEmail());
				if(u != null){
					throw new AppException("O email digitado já está sendo utilizado por outro usuário. Favor verificar se o morador já está cadastrado.");
				}

				usuarioConvite.setCondominio(usuario.getCondominio());
				usuarioConvite.setTipoUsuario(EnumTipoUsuario.MORADOR);
				usuarioConvite.setAdm(false);
				usuarioConvite.setCadastroConfirmado(false);
				usuarioConvite.setDataCadastro(new Date());
				usuarioConvite.setSenhaDigitada("123456");
				usuarioConvite.setSenhaDigitadaConfirmacao("123456");
				usuarioConvite.setArquivo(null);
				usuarioConvite = usuarioService.salvarNovo(usuarioConvite);

				// gerar token
	        	Token token = tokenService.novoToken(String.valueOf(usuarioConvite.getId()));

	        	// enviar email confirmação
	        	try{
	        		enviarEmailConviteVizinho(usuarioConvite, token);
	        	}catch(Exception e){
	        		messageHelper.addInfo("Ocorreu uma falha ao enviar email de convite para seu vizinho!");
	        	}

	        	try{
	        		enviarEmailAvisoCadastro(usuario);
	        	}catch(Exception e){
	        		e.printStackTrace();
	        	}

	        	usuarioConvite = new Usuario();
				messageHelper.addInfo("Convite enviado com suceso!");
		} catch (AppException app){
			throw app;
		} catch (Exception e) {
			throw new AppException("Ocorreu um erro ao salvar seu usuário. Favor tentar novamente.");
		}
	}


	private boolean validarObrigatoriedadeCamposConvite() {
		boolean erro = false;
		if(StringUtils.isBlank(usuarioConvite.getNome())){
			erro = true;
			messageHelper.addError("Você não preencheu o NOME do seu vizinho.");
		}
		if(StringUtils.isBlank(usuarioConvite.getEmail())){
			erro = true;
			messageHelper.addError("Você não preencheu o EMAIL do seu vizinho.");
		}
		return erro;
	}


	private void enviarEmailConviteVizinho(Usuario usuario, Token token) {
		// recuperar url da aplicação
		HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();

		StringBuffer sb = origRequest.getRequestURL().delete(origRequest.getRequestURL().indexOf("listagemUsuario.faces"), origRequest.getRequestURL().toString().length());
		sb.append("confirmarCadastroCondominio.faces?token=").append(token.getToken());

		Map<Object, Object> mapParametrosEmail = new HashMap<Object, Object>();
		mapParametrosEmail.put("nomeUsuario", usuario.getNome());
		mapParametrosEmail.put("nomeCondominio", usuario.getCondominio().getNome());
		mapParametrosEmail.put("link", sb.toString());

		String caminho = arquivoUtil.getCaminhaPastaTemplatesEmail();
		String msg = leitor.processarTemplate(caminho, EnumTemplates.CONFIRMACAO_USUARIO_CONDOMINIO.getNomeArquivo(), mapParametrosEmail);

		Email email = new Email(EnumTemplates.CONFIRMACAO_USUARIO_CONDOMINIO.getDe(), usuario.getEmail(), EnumTemplates.CONFIRMACAO_USUARIO_CONDOMINIO.getAssunto(), msg);
		enviarEmail.enviar(email);
	}


	private void enviarEmailAvisoCadastro(Usuario usuario) {
		StringBuilder sb = new StringBuilder();
		sb.append("Nome usuário : ").append(usuario.getNome()).append("<br />");
		sb.append("ID usuário : ").append(usuario.getId()).append("<br />");
		sb.append("Condominio : ").append(usuario.getCondominio().getNome()).append("<br />");

		Map<Object, Object> mapParametrosEmail = new HashMap<Object, Object>();
		mapParametrosEmail.put("titulo", "CONVITE: Novo usuário do site acaba de ser cadastrado");
		mapParametrosEmail.put("msg", sb.toString());

		String caminho = arquivoUtil.getCaminhaPastaTemplatesEmail();
		String msg = leitor.processarTemplate(caminho, EnumTemplates.PADRAO.getNomeArquivo(), mapParametrosEmail);

		Email email = new Email(EnumTemplates.PADRAO.getDe(), "contatovirtz@gmail.com", EnumTemplates.PADRAO.getAssunto(), msg);
		enviarEmail.enviar(email);
	}




	public void alterarParaSindico(Usuario usuario) throws CondominioException {
		if(usuario != null){
			usuarioService.alterarParaSindico(usuario.getId());
			alterarTipoUsuarioListaCarregada(usuario, EnumTipoUsuario.SINDICO);
			messageHelper.addInfo("Usuário alterado para Síndico!");
		}
	}

	public void alterarParaAdministrativo(Usuario usuario) throws CondominioException {
		if(usuario != null){
			usuarioService.alterarParaAdministrativo(usuario.getId());
			alterarTipoUsuarioListaCarregada(usuario, EnumTipoUsuario.ADMINISTRATIVO);
			messageHelper.addInfo("Usuário alterado para Administrativo!");
		}
	}

	public void alterarParaMorador(Usuario usuario) throws CondominioException {
		if(usuario != null){
			usuarioService.alterarParaMorador(usuario.getId());
			alterarTipoUsuarioListaCarregada(usuario, EnumTipoUsuario.MORADOR);
			messageHelper.addInfo("Usuário alterado para Morador!");
		}
	}

	private void alterarTipoUsuarioListaCarregada(Usuario usuario, EnumTipoUsuario tipo){
		for(Usuario u : usuarios){
			if(u.equals(usuario)){
				u.setTipoUsuario(tipo);
				break;
			}
		}
	}


	public void montarInfoTelaBloqueio(Usuario usuario){
		if(usuario == null){
			return;
		}
		bloqueioUsuarioSelecionado = null;
		this.usuarioSelecionado = usuario;
		List<BloqueioFuncaoUsuario> bloqueios = usuarioService.recuperarBloqueios(usuario.getId());
		if(bloqueios != null && !bloqueios.isEmpty()){
			bloqueioUsuarioSelecionado = bloqueios.get(0);
		} else {
			bloqueioUsuarioSelecionado = new BloqueioFuncaoUsuario();
			bloqueioUsuarioSelecionado.setUsuario(usuarioSelecionado);
			bloqueioUsuarioSelecionado.setFuncaoBloqueio(EnumFuncaoBloqueio.RESERVA);
			bloqueioUsuarioSelecionado.setComentario(EnumFuncaoBloqueio.RESERVA.getMsgParaUsuario());
		}
	}


	public void limparaInfoTelaBloqueio(){
		usuarioSelecionado = null;
		bloqueioUsuarioSelecionado = null;
	}


	public void salvarBloqueioUsuario(){
		if(bloqueioUsuarioSelecionado != null){
			if(!bloqueioUsuarioSelecionado.isMarcado()){
				usuarioService.removerBloqueio(bloqueioUsuarioSelecionado.getUsuario().getId(), bloqueioUsuarioSelecionado.getFuncaoBloqueio());
				messageHelper.addInfo("Bloqueio retirado.");
			} else {
				try {
					usuarioService.bloquearFuncao(bloqueioUsuarioSelecionado.getUsuario(), bloqueioUsuarioSelecionado.getFuncaoBloqueio(), bloqueioUsuarioSelecionado.getComentario());
					messageHelper.addInfo("Bloqueio confirmado.");
				} catch (Exception e) {
					e.printStackTrace();
					messageHelper.addError("Ocorreu um erro ao bloquear funcionalidade para este usuário.");
				}
			}

			usuariosBloqueados = usuarioService.recuperarBloqueiosPorCondominio(usuarioSelecionado.getCondominio().getId(), EnumFuncaoBloqueio.RESERVA);
			limparaInfoTelaBloqueio();
		}
	}


	public boolean usuarioPossuiBloqueios(Usuario usuario){

		if(usuariosBloqueados == null || usuariosBloqueados.isEmpty()){
			return false;
		}
		for(BloqueioFuncaoUsuario bloqueio : usuariosBloqueados){
			if(bloqueio.getUsuario().getId().equals(usuario.getId())){
				return true;
			}
		}

		return false;
	}

	public void changeValueBloqueio(){
		/*if(bloqueioUsuarioSelecionado != null){
			bloqueioUsuarioSelecionado.setMarcado(!bloqueioUsuarioSelecionado.isMarcado());
		}*/
	}


	// GETTERS E SETTERS
	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Usuario getUsuarioSelecionado() {
		return usuarioSelecionado;
	}

	public void setUsuarioSelecionado(Usuario usuarioSelecionado) {
		this.usuarioSelecionado = usuarioSelecionado;
	}

	public BloqueioFuncaoUsuario getBloqueioUsuarioSelecionado() {
		return bloqueioUsuarioSelecionado;
	}

	public Usuario getUsuarioConvite() {
		return usuarioConvite;
	}

	public List<Usuario> getUsuariosFiltrados() {
		return usuariosFiltrados;
	}

	public void setUsuariosFiltrados(List<Usuario> usuariosFiltrados) {
		this.usuariosFiltrados = usuariosFiltrados;
	}


}
