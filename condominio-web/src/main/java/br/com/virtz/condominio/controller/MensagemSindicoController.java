package br.com.virtz.condominio.controller;

import java.util.Date;
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
import br.com.virtz.condominio.email.EnviarEmail;
import br.com.virtz.condominio.email.template.LeitorTemplate;
import br.com.virtz.condominio.entidades.MensagemSindico;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.service.IMensagemSindicoService;
import br.com.virtz.condominio.service.IUsuarioService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.ArquivosUtil;
import br.com.virtz.condominio.util.MessageHelper;

@ManagedBean
@ViewScoped
public class MensagemSindicoController {

	@EJB
	private IMensagemSindicoService mensagemService;
	
	@Inject
	private MessageHelper messageHelper;
	
	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private LeitorTemplate leitor;
	
	@EJB
	private IUsuarioService usuarioService;
	
	@EJB
	private EnviarEmail enviarEmail;
	
	@Inject
	private ArquivosUtil arquivosUtil;
	
	
	private String mensagem;
	private Usuario usuario;
	
	
	@PostConstruct
	public void init(){
		mensagem = null;
		usuario = sessao.getUsuarioLogado();
	}
	
	
		
	public void enviarMensagem(ActionEvent actionEvent) {
		if(StringUtils.isNotBlank(this.mensagem)){
			MensagemSindico msg = new MensagemSindico();
			msg.setCondominio(usuario.getCondominio());
			msg.setData(new Date());
			msg.setMensagem(this.mensagem);
			msg.setUsuario(usuario);
			try {
				msg = mensagemService.salvar(msg);
				messageHelper.addInfo("Sua mensagem enviada para o sindico!");
				
				this.mensagem = null;
				try {
					List<Usuario> sindicos = usuarioService.recuperarSindicos(usuario.getCondominio().getId());
					envioEmail(sindicos, msg);
				}catch(Exception e1){
					messageHelper.addError("Sua mensagem foi salva porém ocorreu um erro ao enviar o email para o sindíco.");	
				}
				
			} catch (Exception e) {
				messageHelper.addError("Ocorreu um erro ao enviar sua mensagem.");
			}
		}
    }

	

	private void envioEmail(List<Usuario> sindicos, MensagemSindico msg) {
		for(Usuario sindico : sindicos){
			Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("nome_sindico", sindico.getNome());
			map.put("nome_usuario", msg.getUsuario().getNome());
			map.put("msg", msg.getMensagem());
			
			String caminho = arquivosUtil.getCaminhaPastaTemplatesEmail();
			String msgEnviar = leitor.processarTemplate(caminho, EnumTemplates.MENSAGEM_SINDICO.getNomeArquivo(), map);
			
			Email email = new Email(EnumTemplates.MENSAGEM_SINDICO.getDe(), sindico.getEmail(), EnumTemplates.MENSAGEM_SINDICO.getAssunto(), msgEnviar);
			enviarEmail.enviar(email);
		}
		
	}

	

	// GETTERS E SETTERS
	
	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
		
}
