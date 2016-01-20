package br.com.virtz.condominio.controller;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import br.com.virtz.condominio.entidades.PautaAssembleia;
import br.com.virtz.condominio.entidades.Token;
import br.com.virtz.condominio.exception.ErroAoSalvar;
import br.com.virtz.condominio.service.IAssembleiaService;
import br.com.virtz.condominio.service.ITokenService;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class ConfirmarEsqueciSenhaController {
	
	@EJB
	private IAssembleiaService assembleiaService;
	
	@EJB
	private ITokenService tokenService;
	
	@Inject
	private NavigationPage navegacao;
	
	@Inject
	private MessageHelper messageHelper;
	

	private String token = null;
	
	private PautaAssembleia pauta = null;
	
	
	
	@PostConstruct
	public void init(){
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest(); 
 
		token = request.getParameter("token");
		
		// verifica se o token é valido
		if(tokenService.tokenEhValido(token)){
			Token tokenEntidade = tokenService.recuperarToken(token);
			
			// recuperar o id da paula para busca-la no banco
			String chave = tokenEntidade.getChaveEntidade();
			if(StringUtils.isNotBlank(chave)){
				pauta = assembleiaService.recuperarPautaPorId(Long.valueOf(chave));
				if(pauta != null){
					tokenService.invalidar(token);
					try {
						pauta.setAprovada(Boolean.TRUE);
						assembleiaService.salvarPauta(pauta);
						messageHelper.addInfo("A pauta foi aprovada com sucesso!");
					} catch (ErroAoSalvar e) {
						e.printStackTrace();
					}
				}
			} 
		} 
	}
	
	
	public void irParaLogin(){
		navegacao.redirectToPage("/login.faces");
	}



	
	/* GETTERS e SETTERS*/

	public PautaAssembleia getPauta() {
		return pauta;
	}

	public void setPauta(PautaAssembleia pauta) {
		this.pauta = pauta;
	}

}
