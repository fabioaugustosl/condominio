package br.com.virtz.condominio.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import br.com.virtz.condominio.entidades.BatePapo;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.ErroAoSalvar;
import br.com.virtz.condominio.service.IBatePapoService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.MessageHelper;

@ManagedBean
@ViewScoped
public class BatePapoController {

	@EJB
	private IBatePapoService batePapoService;
	
	@Inject
	private MessageHelper messageHelper;
	
	@Inject
	private SessaoUsuario sessao;
	
	private List<BatePapo> batePapos;
	private String mensagem;
	
	@PostConstruct
	public void init(){
		mensagem = null;
		batePapos = listarTodos(); 
	}
	
	
	public List<BatePapo> listarTodos(){
		Usuario usuario = sessao.getUsuarioLogado();
		
		List<BatePapo> batePapos = batePapoService.recuperarTodos(usuario.getCondominio());
		for(BatePapo bp : batePapos){
			bp.calcularAvalicoesPositivasENegativas();
		}
		
		return batePapos;
	}

	public void positivar(BatePapo batePapo){
		try {
			batePapoService.avaliarNegativamente(batePapo, sessao.getUsuarioLogado(), null);
		} catch (ErroAoSalvar e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void negativar(BatePapo batePapo){
		try {
			batePapoService.avaliarNegativamente(batePapo, sessao.getUsuarioLogado(), null);
		} catch (ErroAoSalvar e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void enviarMensagem(ActionEvent actionEvent) {
		if(StringUtils.isNotBlank(this.mensagem)){
			BatePapo batePapo = new BatePapo();
			batePapo.setMensagem(mensagem);
			batePapo.setCondominio(sessao.getUsuarioLogado().getCondominio());
			batePapo.setUsuario(sessao.getUsuarioLogado());
			try {
				batePapoService.salvar(batePapo);
				messageHelper.addInfo("Mensagem enviada!");
				this.mensagem = null;
			} catch (Exception e) {
				messageHelper.addError("Ocorreu um erro ao enviar sua mensagem");
			}
		}
    }


	// GETTERS E SETTERS
	public List<BatePapo> getBatePapos() {
		return batePapos;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
		
}
