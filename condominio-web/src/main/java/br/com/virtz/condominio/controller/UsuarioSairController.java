package br.com.virtz.condominio.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.apache.shiro.SecurityUtils;

import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class UsuarioSairController implements Serializable{
	
	private static final long serialVersionUID = 1L;

	
	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private NavigationPage navegacao;
	
	
	@PostConstruct
	public void init(){
		
	}
	
	public void sair(){
		sessao.setUsuarioLogado(null);
		SecurityUtils.getSubject().logout();
		navegacao.redirectToPage("/login.faces");
	}
	
	public void nao(){
		navegacao.redirectToPage("/portal.faces");
	}

	
}
