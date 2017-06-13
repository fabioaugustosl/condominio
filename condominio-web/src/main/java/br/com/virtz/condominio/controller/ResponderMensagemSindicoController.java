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
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import br.com.virtz.condominio.bean.Email;
import br.com.virtz.condominio.constantes.EnumTemplates;
import br.com.virtz.condominio.email.EnviarEmail;
import br.com.virtz.condominio.email.template.LeitorTemplate;
import br.com.virtz.condominio.entidades.MensagemSindico;
import br.com.virtz.condominio.entidades.RespostaMensagemSindico;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exceptions.CondominioException;
import br.com.virtz.condominio.service.IMensagemSindicoService;
import br.com.virtz.condominio.service.IPublicidadeService;
import br.com.virtz.condominio.service.IUsuarioService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.IArquivosUtil;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class ResponderMensagemSindicoController {

	@EJB
	private IUsuarioService usuarioService;

	@EJB
	private IMensagemSindicoService mensagemService;

	@Inject
	private SessaoUsuario sessao;

	@Inject
	private MessageHelper msgHelper;

	@EJB
	private EnviarEmail enviarEmail;

	@EJB
	private IPublicidadeService publicidadeService;
	
	@Inject
	private IArquivosUtil arquivoUtil;

	@Inject
	private LeitorTemplate leitor;

	@Inject
	private NavigationPage navegacao;



	private String mensagem = null;
	private Boolean enviarParaTodos = null;
	private MensagemSindico mensagemSindico = null;

	private Usuario usuario = null;


	@PostConstruct
	public void init(){
		enviarParaTodos = Boolean.TRUE;
		usuario = sessao.getUsuarioLogado();

		Object idMensagem = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("idMensagem");
		if(idMensagem != null){
			mensagemSindico = mensagemService.recuperarMensagemSindico(Long.valueOf(idMensagem.toString()));

			StringBuilder sb = new StringBuilder("<p><b>Mensagem enviado por uma morador: </b></p><p>").append(mensagemSindico.getMensagem()).append("</p>");
			sb.append("<br ><p><b>Resposta do síndico: </b></p><br />");

			mensagem = sb.toString();
		}
		
		leitor.setPublicidadeService(publicidadeService);
	}


	public List<Usuario> listarTodos(){
		return usuarioService.recuperarTodos(usuario.getCondominio());
	}


	public void enviarEmail(ActionEvent event) throws CondominioException {
		if(StringUtils.isBlank(this.mensagem)){
			throw new CondominioException("Você não preencheu a mensagem a ser enviada.");
		}

		Map<Object, Object> mapParametrosEmail = new HashMap<Object, Object>();
		mapParametrosEmail.put("msg", this.mensagem);

		String msg = leitor.processarTemplate(usuario.getCondominio().getId(), arquivoUtil.getCaminhaPastaTemplatesEmail(), EnumTemplates.RESPOSTA_MENSAGEM_SINDICO.getNomeArquivo(), mapParametrosEmail);

		List<Usuario> usuarios = null;
		if(enviarParaTodos){
			usuarios = listarTodos();
		} else {
			usuarios = new ArrayList<Usuario>();
			usuarios.add(mensagemSindico.getUsuario());
		}

		try {
			RespostaMensagemSindico resposta = new RespostaMensagemSindico();
			resposta.setData(new Date());
			resposta.setMensagem(this.mensagem);
			resposta.setMensagemSindico(mensagemSindico);
			resposta.setRespostaParaTodos(enviarParaTodos);
			resposta.setUsuario(sessao.getUsuarioLogado());
			mensagemService.salvar(resposta);

			for(Usuario u : usuarios){
				Email email = new Email(EnumTemplates.RESPOSTA_MENSAGEM_SINDICO.getDe(), u.getEmail(), EnumTemplates.RESPOSTA_MENSAGEM_SINDICO.getDe(), msg);
				enviarEmail.enviar(email);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		msgHelper.addInfo("Sua mensagem foi enviada!");
		irParaListagem();
	}


	public void irParaListagem(){
		navegacao.redirectToPage("/sindico/listagemMensagemSindico.faces");
	}



	// GETTERS E SETTERS
	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public Boolean getEnviarParaTodos() {
		return enviarParaTodos;
	}

	public void setEnviarParaTodos(Boolean enviarParaTodos) {
		this.enviarParaTodos = enviarParaTodos;
	}


}
