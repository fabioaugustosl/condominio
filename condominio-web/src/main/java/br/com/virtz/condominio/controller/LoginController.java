package br.com.virtz.condominio.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import br.com.virtz.condominio.bean.Email;
import br.com.virtz.condominio.constantes.EnumTemplates;
import br.com.virtz.condominio.email.EnviarEmailPadrao;
import br.com.virtz.condominio.email.template.LeitorTemplate;
import br.com.virtz.condominio.entidades.AreaComum;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.geral.ParametroSistemaLookup;
import br.com.virtz.condominio.service.ICondominioService;
import br.com.virtz.condominio.service.IUsuarioService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.ArquivosUtil;
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
	
	@Inject
	private ParametroSistemaLookup parametroLookup;
	
	private String login;
	
	@Inject
	private LeitorTemplate leitor;
	
	@Inject
	private ArquivosUtil arquivosUtil;
	
	/*@EJB
	private EnviarEmailPadrao enviarEmail;
	*/
	
	public void logar() throws Exception{
		
		//metodoAuxiliarCriacaoUsuarioDesenv();
				
		Usuario u = usuarioService.recuperarUsuarioCompleto(1l);
		sessao.setUsuarioLogado(u);
		
		// iniciar lookups
		parametroLookup.iniciarLookup(u.getCondominio());
		
		//testeEnvioEmail();
		
		navigation.redirectToPage("/portal.faces");
	}


	private void metodoAuxiliarCriacaoUsuarioDesenv() throws Exception {
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
		
		ac = condominioService.salvarAreaComum(ac);
		
		c.setAreasComuns(new HashSet<AreaComum>());
		c.getAreasComuns().add(ac);
		
		u.setCondominio(c);
		
		u = usuarioService.salvar(u);
	}


	private void testeEnvioEmail() {
		Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("titulo", "Fábio");
        
        String caminho = arquivosUtil.getCaminhaPastaTemplatesEmail();
		String msg = leitor.processarTemplate(caminho, EnumTemplates.PADRAO.getNomeArquivo(), map);
		
		Email email = new Email("fabioaugustosl@gmail.com", "fabioaugustosl@gmail.com", "teste Fabio", msg);
//		enviarEmail.enviar(email);
	}


	public String getLogin() {
		return login;
	}


	public void setLogin(String login) {
		this.login = login;
	}
	
	
	
		
}
