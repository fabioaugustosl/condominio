package br.com.virtz.condominio.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import br.com.virtz.condominio.bean.Email;
import br.com.virtz.condominio.constantes.EnumTemplates;
import br.com.virtz.condominio.constantes.EnumTipoNotificacao;
import br.com.virtz.condominio.email.EnviarEmail;
import br.com.virtz.condominio.email.template.LeitorTemplate;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.exceptions.CondominioException;
import br.com.virtz.condominio.service.INotificacaoService;
import br.com.virtz.condominio.service.IUsuarioService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.IArquivosUtil;
import br.com.virtz.condominio.util.MessageHelper;

@ManagedBean
@ViewScoped
public class EnviarMensagemUsuarioController {

	@EJB
	private IUsuarioService usuarioService;

	@EJB
	private INotificacaoService notificacaoService;

	@Inject
	private SessaoUsuario sessao;

	@Inject
	private MessageHelper msgHelper;

	@Inject
	private IArquivosUtil arquivoUtil;

	@Inject
	private LeitorTemplate leitor;


	@EJB
	private EnviarEmail enviarEmail;


	private List<Usuario> usuarios = null;
	private List<Usuario> usuariosSelecionados = null;
	private String mensagem = null;
	private String assunto = null;

	private Usuario usuario = null;


	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
		usuarios = listarTodos();
		usuariosSelecionados = new ArrayList<Usuario>();
	}


	public List<Usuario> listarTodos(){
		List<Usuario> usuarios = usuarioService.recuperarTodos(usuario.getCondominio());
		return usuarios;
	}


	public void enviarEmail(ActionEvent event) throws CondominioException, UnsupportedEncodingException {
		if(StringUtils.isBlank(this.assunto)){
			throw new CondominioException("Você não preencheu o assunto da mensagem.");
		}
		if(StringUtils.isBlank(this.mensagem)){
			throw new CondominioException("Você não preencheu a mensagem a ser enviada.");
		}
		if(usuariosSelecionados == null || usuariosSelecionados.isEmpty()){
			throw new CondominioException("É necessário preencher pelo menos um condômino.");
		}

		Map<Object, Object> mapParametrosEmail = new HashMap<Object, Object>();
		mapParametrosEmail.put("titulo", new String(this.assunto.getBytes("ISO-8859-1")));
		mapParametrosEmail.put("msg", new String(this.mensagem.getBytes("ISO-8859-1")));

		String msg = leitor.processarTemplate( arquivoUtil.getCaminhaPastaTemplatesEmail(), EnumTemplates.PADRAO.getNomeArquivo(), mapParametrosEmail);

		for(Usuario u : usuariosSelecionados){
			Email email = new Email(EnumTemplates.PADRAO.getDe(), u.getEmail(), new String(this.assunto.getBytes("ISO-8859-1")), msg);
			enviarEmail.enviar(email);
		}

		resetarDadosTela();
		msgHelper.addInfo("Sua mensagem foi enviada aos condôminos selecionados!");
	}


	public void enviarNotificacao(ActionEvent event) throws CondominioException {
		if(StringUtils.isBlank(this.mensagem)){
			throw new CondominioException("Você não preencheu a mensagem a ser enviada.");
		}
		if(usuariosSelecionados == null || usuariosSelecionados.isEmpty()){
			throw new CondominioException("É necessário preencher pelo menos um condômino.");
		}

		for(Usuario u : usuariosSelecionados){
			try {
				notificacaoService.salvarNovaNotificacao(usuario.getCondominio(), u, EnumTipoNotificacao.AVISO, this.mensagem);
			} catch (AppException e) {
				msgHelper.addInfo("Ocorreu um erro ao enviar notificação para o usuário: "+u.getNome());
			}
		}

		resetarDadosTela();
		msgHelper.addInfo("Sua mensagem foi enviada aos condôminos selecionados!");
	}


	private void resetarDadosTela() {
		usuariosSelecionados = new ArrayList<Usuario>();
		this.assunto = null;
		this.mensagem = null;
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
