package br.com.virtz.condominio.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
import org.primefaces.event.FileUploadEvent;

import br.com.virtz.condominio.bean.Email;
import br.com.virtz.condominio.constantes.EnumTemplates;
import br.com.virtz.condominio.constantes.EnumTipoNotificacao;
import br.com.virtz.condominio.email.EnviarEmail;
import br.com.virtz.condominio.email.template.LeitorTemplate;
import br.com.virtz.condominio.entidades.ArquivoDocumento;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.exceptions.CondominioException;
import br.com.virtz.condominio.service.INotificacaoService;
import br.com.virtz.condominio.service.IUsuarioService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.ArquivosUtil;
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

	@Inject
	private ArquivosUtil arquivosUtil;


	@EJB
	private EnviarEmail enviarEmail;


	private List<Usuario> usuarios = null;
	private List<Usuario> usuariosSelecionados = null;
	private String mensagem = null;
	private String assunto = null;

	private Usuario usuario = null;
	//anexo email
	private String nomeArqAnexo = null;
	private InputStream inputStreamArquivo = null;

	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
		usuarios = listarTodos();
		usuariosSelecionados = new ArrayList<Usuario>();
		nomeArqAnexo = null;
		inputStreamArquivo = null;
	}


	public List<Usuario> listarTodos(){
		List<Usuario> usuarios = usuarioService.recuperarTodos(usuario.getCondominio());
		return usuarios;
	}


	public void enviarEmail(ActionEvent event) throws CondominioException, UnsupportedEncodingException {
		if(StringUtils.isBlank(this.assunto)){
			msgHelper.addError("Você não preencheu o assunto da mensagem.");
		}
		if(StringUtils.isBlank(this.mensagem)){
			msgHelper.addError("Você não preencheu a mensagem a ser enviada.");
		}
		if(usuariosSelecionados == null || usuariosSelecionados.isEmpty()){
			msgHelper.addError("É necessário preencher pelo menos um condômino.");
		}

		byte[] anexo = null;
		if(inputStreamArquivo != null){
			anexo = arquivosUtil.converter(inputStreamArquivo);
		}

		Map<Object, Object> mapParametrosEmail = new HashMap<Object, Object>();
		mapParametrosEmail.put("titulo", this.assunto);
		mapParametrosEmail.put("msg", this.mensagem);

		String msg = leitor.processarTemplate( arquivoUtil.getCaminhaPastaTemplatesEmail(), EnumTemplates.PADRAO.getNomeArquivo(), mapParametrosEmail);

		for(Usuario u : usuariosSelecionados){
			Email email = new Email(EnumTemplates.PADRAO.getDe(), u.getEmail(), new String(this.assunto), msg);
			if(anexo != null){
				email.setAnexo(anexo);
				email.setNomeAnexo(nomeArqAnexo);
			}
			enviarEmail.enviar(email);
		}

		resetarDadosTela();
		msgHelper.addInfo("Sua mensagem foi enviada aos condôminos selecionados!");

		nomeArqAnexo = null;
		inputStreamArquivo = null;
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

	public void removerArquivo(ActionEvent event) throws CondominioException {
		if(StringUtils.isNotBlank(nomeArqAnexo)){
			//File arquivoDeletar = new File(arquivoUtil.getCaminhoArquivosUpload()+"\\"+nomeArqAnexo);
			//arquivoDeletar.delete();
			nomeArqAnexo = null;
			msgHelper.addInfo("Anexo removido com sucesso!");
			inputStreamArquivo = null;
		}
	}


	public void uploadArquivo(FileUploadEvent event) {
        try {
			inputStreamArquivo = event.getFile().getInputstream();
			nomeArqAnexo = event.getFile().getFileName();

			//String caminho = arquivoUtil.getCaminhoArquivosUpload();
			//String nomeAntigo = event.getFile().getFileName();
			//String extensao = arquivoUtil.pegarExtensao(nomeAntigo);
			//String nomeNovo = arquivoUtil.gerarNomeArquivo(extensao, ArquivosUtil.TIPO_ARQUIVO_DOCUMENTO);

			//ArquivoDocumento arqDocumento = new ArquivoDocumento();
			//arqDocumento.setCaminho(caminho);
			//arqDocumento.setExtensao(extensao);
			//arqDocumento.setNomeOriginal(nomeAntigo);
			//arqDocumento.setTamanho(event.getFile().getSize());
			//arqDocumento.setNome(nomeNovo);

			//arquivoUtil.arquivar(inputStream, nomeNovo);


			msgHelper.addInfo("Arquivo "+nomeArqAnexo+" foi anexado com sucesso.");
        } catch (IOException e) {
            msgHelper.addError("Ocorreu um erro ao fazer upload do arquivo. Favor acessar novamente na tela.");
        }
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

	public String getNomeArqAnexo() {
		return nomeArqAnexo;
	}

	public void setNomeArqAnexo(String nomeArqAnexo) {
		this.nomeArqAnexo = nomeArqAnexo;
	}


}
