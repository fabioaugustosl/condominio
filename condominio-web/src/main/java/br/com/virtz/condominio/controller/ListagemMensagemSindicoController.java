package br.com.virtz.condominio.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import br.com.virtz.condominio.entidades.MensagemSindico;
import br.com.virtz.condominio.entidades.Noticia;
import br.com.virtz.condominio.entidades.RespostaMensagemSindico;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exceptions.CondominioException;
import br.com.virtz.condominio.service.IMensagemSindicoService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class ListagemMensagemSindicoController {

	@EJB
	private IMensagemSindicoService mensagemSindicoService;

	@Inject
	private MessageHelper messageHelper;

	@Inject
	private SessaoUsuario sessao;

	@Inject
	private NavigationPage navegacao;

	private List<MensagemSindico> mensagens;
	private MensagemSindico mensagemSelecionada = null;
	private Usuario usuario = null;
	private String resposta = null;


	@PostConstruct
	public void init(){
		usuario =  sessao.getUsuarioLogado();
		mensagens = listarTodos();
	}


	public List<MensagemSindico> listarTodos(){
		List<MensagemSindico> lista = mensagemSindicoService.recuperarTodos(usuario.getCondominio().getId());
		return lista;
	}


	public void removerMensagem(MensagemSindico msg) throws CondominioException {
		if(msg != null){
			mensagemSindicoService.remover(msg.getId());
			messageHelper.addInfo("Mensagem removida com sucesso!");
			mensagens.remove(msg);
		}
	}


	public void enviarResposta(Long idMensagem) throws CondominioException {
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("idMensagem", idMensagem);
		navegacao.redirectToPage("/sindico/responderMensagemSindico.faces");
	}


	public void verResposta(MensagemSindico mensagem) throws CondominioException {
		if(mensagem.getRespostas() != null && !mensagem.getRespostas().isEmpty()){
			resposta = mensagem.getRespostas().get(0).getMensagem();
		}
		resposta = null;
	}



	// GETTERS E SETTERS
	public List<MensagemSindico> getMensagens() {
		return mensagens;
	}

	public void setMensagens(List<MensagemSindico> mensagens) {
		this.mensagens = mensagens;
	}

	public String getResposta() {
		return resposta;
	}

	public MensagemSindico getMensagemSelecionada() {
		return mensagemSelecionada;
	}

	public void setMensagemSelecionada(MensagemSindico mensagemSelecionada) {
		this.mensagemSelecionada = mensagemSelecionada;
	}


}
