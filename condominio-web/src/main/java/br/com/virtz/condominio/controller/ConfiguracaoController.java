package br.com.virtz.condominio.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import org.primefaces.event.CloseEvent;
import org.primefaces.event.DashboardReorderEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;

import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class ConfiguracaoController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private NavigationPage navegacao;
	
	@Inject
	private MessageHelper messageHelper;

	@Inject
	private SessaoUsuario sessao;

	private Usuario usuario = null;
	private String senhaAtual = null;
	private String senhaNova = null;
	private String senhaNova2 = null;

	@PostConstruct
	public void init() {
		usuario = sessao.getUsuarioLogado();
	}
	
	
	public void alterarSenha(ActionEvent event){
		messageHelper.addInfo("Senha alterada com sucesso.");
	}
	

	public void irParaPrincipal() {
		navegacao.redirectToPage("/portal.faces");
	}

	public String getSenhaAtual() {
		return senhaAtual;
	}

	public void setSenhaAtual(String senhaAtual) {
		this.senhaAtual = senhaAtual;
	}

	public String getSenhaNova() {
		return senhaNova;
	}

	public void setSenhaNova(String senhaNova) {
		this.senhaNova = senhaNova;
	}

	public String getSenhaNova2() {
		return senhaNova2;
	}

	public void setSenhaNova2(String senhaNova2) {
		this.senhaNova2 = senhaNova2;
	}

}
