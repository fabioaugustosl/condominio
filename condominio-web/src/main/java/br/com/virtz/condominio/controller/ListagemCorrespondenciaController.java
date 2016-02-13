package br.com.virtz.condominio.controller;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.model.LazyDataModel;

import br.com.virtz.condominio.constantes.EnumTipoUsuario;
import br.com.virtz.condominio.controller.lazy.RecebidoLazyModel;
import br.com.virtz.condominio.entidades.Recebido;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.service.IRecebidoService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class ListagemCorrespondenciaController {
	
	@EJB
	private IRecebidoService recebidoService;
	
	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private MessageHelper message;
	
	@Inject
	private NavigationPage navegacao;
	
		
	private LazyDataModel<Recebido> recebidos = null;
	
	private Usuario usuario = null;
	private Recebido recebidoRetirada = null;
	private String nomeRetirada = null;
	
	
	
	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
	}
	
	
	public LazyDataModel<Recebido> getRecebidos(){
		if(recebidos == null){
			recebidos = new RecebidoLazyModel(recebidoService.recuperarPorCondominioPaginado(usuario.getCondominio().getId(), 0, 50), usuario.getCondominio().getId(),usuario.getApartamento().getId(), recebidoService);
		}
			
		return recebidos;
	}
	
	
	public boolean superUsuario(){
		if(EnumTipoUsuario.MORADOR.equals(usuario.getTipoUsuario())){
			return false;
		}
		return true;
	}
	
	public void irParaCadastro(){
		navegacao.redirectToPage("/portaria/cadastrarRecebido.faces");
	}
			
			
	
	public void retirada(){
		if(recebidoRetirada != null){
			try {
				recebidoService.salvarRetirada(recebidoRetirada.getId(), nomeRetirada);
				message.addInfo("Retirada de correnspondência/encomenda registrada!");
				recebidoRetirada = null;
				nomeRetirada = null;
			} catch (AppException e) {
				e.printStackTrace();
				message.addError("Ocorreu um erro ao salvar a retirada.");
			}
		} else {
			message.addError("Ocorreu um erro ao identificar a correspondência/encomenda para dar baixa. Favor tentar novamente.");
		}
	}
	
	
	
	// GETTERS E SETTERS
	
	public Recebido getRecebidoRetirada() {
		return recebidoRetirada;
	}

	public void setRecebidoRetirada(Recebido recebidoRetirada) {
		this.recebidoRetirada = recebidoRetirada;
	}

	public String getNomeRetirada() {
		return nomeRetirada;
	}

	public void setNomeRetirada(String nomeRetirada) {
		this.nomeRetirada = nomeRetirada;
	}
	
	
}

