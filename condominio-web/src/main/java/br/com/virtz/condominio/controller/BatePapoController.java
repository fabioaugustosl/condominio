package br.com.virtz.condominio.controller;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import br.com.virtz.condominio.constantes.EnumTipoUsuario;
import br.com.virtz.condominio.entidades.Avaliacao;
import br.com.virtz.condominio.entidades.BatePapo;
import br.com.virtz.condominio.entidades.Condominio;
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
	private Usuario usuario;
	
	@PostConstruct
	public void init(){
		mensagem = null;
		usuario = sessao.getUsuarioLogado();
		batePapos = listarTodos(usuario.getCondominio());
	}
	
	
	public List<BatePapo> listarTodos(Condominio condominio){
		
		List<BatePapo> batePapos = batePapoService.recuperarTodos(condominio);
		contarAvaliacoes(batePapos);
		
		return batePapos;
	}


	private void contarAvaliacoes(List<BatePapo> batePapos) {
		for(BatePapo bp : batePapos){
			bp.calcularAvalicoesPositivasENegativas();
		}
	}

	public void positivar(BatePapo batePapo){
		try {
			Avaliacao avaliacao = batePapoService.avaliarPositivamente(batePapo, sessao.getUsuarioLogado(), null);
			adicionarNovaAvaliacao(batePapo, avaliacao);
			contarAvaliacoes(batePapos);
		} catch (ErroAoSalvar e) {
			e.printStackTrace();
			messageHelper.addError("Ocorreu um erro ao positivar mensagem.");
		}
	}


	public void negativar(BatePapo batePapo){
		try {
			Avaliacao avaliacao = batePapoService.avaliarNegativamente(batePapo, sessao.getUsuarioLogado(), null);
			adicionarNovaAvaliacao(batePapo, avaliacao);
			contarAvaliacoes(batePapos);
		} catch (ErroAoSalvar e) {
			e.printStackTrace();
			messageHelper.addError("Ocorreu um erro ao negativar mensagem.");
		}
	}
	
	

	private void adicionarNovaAvaliacao(BatePapo batePapo, Avaliacao avaliacao) {
		for(BatePapo bp : batePapos){
			if(bp.equals(batePapo)){
				if(bp.getAvaliacoes() == null){
					bp.setAvaliacoes(new HashSet<Avaliacao>());
				}
				bp.getAvaliacoes().add(avaliacao);
			}
		}
	}
	
	
	public boolean podeExcluir(BatePapo papo){
		 if(papo.getUsuario().getId().equals(usuario.getId()) || EnumTipoUsuario.SINDICO.equals(usuario.getTipoUsuario())){
			 return Boolean.TRUE;
		 }
		 return Boolean.FALSE;
	 }
	 
	 public void excluir(BatePapo papo){
		 if(papo != null){
			 batePapos.remove(papo);
			 batePapoService.remover(papo.getId());
			 messageHelper.addInfo("Mensagem removida!");
		 }
	 }
	 
	
	public void enviarMensagem(ActionEvent actionEvent) {
		if(StringUtils.isNotBlank(this.mensagem)){
			BatePapo batePapo = new BatePapo();
			batePapo.setMensagem(mensagem);
			batePapo.setCondominio(sessao.getUsuarioLogado().getCondominio());
			batePapo.setUsuario(sessao.getUsuarioLogado());
			batePapo.setData(new Date());
			try {
				batePapo = batePapoService.salvar(batePapo);
				batePapos.add(batePapo);
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
