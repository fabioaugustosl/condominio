package br.com.virtz.condominio.controller;

import java.util.HashSet;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.print.attribute.HashAttributeSet;

import org.apache.commons.lang.StringUtils;

import br.com.virtz.condominio.entity.AreaComum;
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
	
	private String login;
	
	
	public void logar() throws Exception{
		Usuario u = new Usuario();
		u.setEmail("fabioaugustosl@gmail.com");
		u.setNome("Fabio");
		
		if(StringUtils.isNotBlank(login) && login.equalsIgnoreCase("sindico") ){
			u.setSindico(Boolean.TRUE);
		}
		
		Condominio c = new Condominio();
		c.setNome("Ponto Imperial");
		
		c = condominioService.salvar(c);
		AreaComum ac = new AreaComum();
		ac.setNome("Churrasqueira");
		ac.setCondominio(c);
		c.setAreasComuns(new HashSet<AreaComum>());
		c.getAreasComuns().add(ac);
		
		u.setCondominio(c);
		
		u = usuarioService.salvar(u);
		
		sessao.setUsuarioLogado(u);
		
		navigation.redirectToPage("/portal.faces");
	}


	public String getLogin() {
		return login;
	}


	public void setLogin(String login) {
		this.login = login;
	}
	
	
	
		
}
