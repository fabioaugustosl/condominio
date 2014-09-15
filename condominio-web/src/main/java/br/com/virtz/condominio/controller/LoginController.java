package br.com.virtz.condominio.controller;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import br.com.virtz.condominio.entity.Condominio;
import br.com.virtz.condominio.entity.Usuario;
import br.com.virtz.condominio.service.ICondominioService;
import br.com.virtz.condominio.service.IUsuarioService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class LoginController {

	@Inject
	private NavigationPage navigation;
	
	@EJB
	private IUsuarioService usuarioService;
	
	@EJB
	private ICondominioService condominioService;
	
	@Inject
	private SessaoUsuario sessao;
	
	
	public void logar() throws Exception{
		Usuario u = new Usuario();
		u.setEmail("fabioaugustosl@gmail.com");
		u.setNome("Fabio");
		
		Condominio c = new Condominio();
		c.setNome("Ponto Imperial");
		
		c = condominioService.salvar(c);
		
		u.setCondominio(c);
		
		u = usuarioService.salvar(u);
		
		sessao.setUsuarioLogado(u);
		
		navigation.redirectToPage("/portal.faces");
	}
		
}
