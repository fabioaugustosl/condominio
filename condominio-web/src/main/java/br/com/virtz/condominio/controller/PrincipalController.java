package br.com.virtz.condominio.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import br.com.virtz.condominio.constantes.EnumTipoUsuario;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.service.ICondominioService;
import br.com.virtz.condominio.session.SessaoUsuario;


@ManagedBean
@SessionScoped
public class PrincipalController implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB
	private ICondominioService condominioService;

	@Inject
	private SessaoUsuario sessao;

	private Condominio condominio = null;
	private Usuario usuario = null;
	private Boolean condominioPossuiAgrupamento = null;



	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
		condominio = usuario.getCondominio();
	}

	public void onIdle() {
		// fazer nada
    }
	
	public boolean ehSindico(){
		 if(EnumTipoUsuario.SINDICO.equals(sessao.getUsuarioLogado().getTipoUsuario())){
			 return Boolean.TRUE;
		 }
		 return Boolean.FALSE;
	}


	public boolean ehPorteiro(){
		 if(EnumTipoUsuario.PORTEIRO.equals(sessao.getUsuarioLogado().getTipoUsuario())){
			 return Boolean.TRUE;
		 }
		 return Boolean.FALSE;
	}


	public boolean ehAdministrativo(){
		 if(EnumTipoUsuario.ADMINISTRATIVO.equals(sessao.getUsuarioLogado().getTipoUsuario())){
			 return Boolean.TRUE;
		 }
		 return Boolean.FALSE;
	}


	public boolean ehAdministrador(){
		 if(EnumTipoUsuario.ADMINISTRADOR.equals(sessao.getUsuarioLogado().getTipoUsuario())){
			 return Boolean.TRUE;
		 }
		 return Boolean.FALSE;
	}


	public boolean exibirMenu(String menu){
		if("mensagemMoradores".equals(menu)){
			return this.ehSindico() || this.ehAdministrativo();
		}
		else if("assembleias".equals(menu)){
			return !ehPorteiro();
		}
		else if("documentos".equals(menu)){
			return true;
		}
		else if("indicacao".equals(menu)){
			return !ehPorteiro() && !ehAdministrador();
		}
		else if("reservar".equals(menu)){
			return !ehAdministrador();
		}
		else if("votacao".equals(menu)){
			return ehSindico();
		}
		else if("ocorrencia".equals(menu)){
			return !ehPorteiro() && !ehAdministrador();
		}
		else if("moradores".equals(menu)){
			return true;
		}
		else if("noticias".equals(menu)){
			return true;
		}
		else if("mensagemSindico".equals(menu)){
			return ehSindico();
		}
		else if("boletos".equals(menu)){
			return !ehPorteiro();
		}
		else if("receitasDespesas".equals(menu)){
			return ehSindico() || ehAdministrador();
		}
		else if("prestacaoContas".equals(menu)){
			return !ehPorteiro();
		}
		else if("porteiros".equals(menu)){
			return ehSindico();
		}
		else if("administrador".equals(menu)){
			return ehSindico();
		}
		else if("portaria".equals(menu)){
			return ehSindico() || ehPorteiro() || ehAdministrativo();
		}
		else if("notificarPortaria".equals(menu)){
			return !ehPorteiro() && !ehAdministrador();
		}
		else if("correspondecias".equals(menu)){
			return !ehPorteiro() && !ehAdministrador();
		}
		else if("minhasVisitas".equals(menu)){
			return !ehPorteiro() && !ehAdministrador();
		}
		else if("meuCondominio".equals(menu)){
			return ehSindico();
		}
		else if("gerais".equals(menu)){
			return ehSindico();
		}
		else if("atalho".equals(menu)){
			return ehSindico();
		}

		return false;
	}


	public boolean possuiCFTB(){
		if(getCondominio().getCftv() != null && !StringUtils.isEmpty(getCondominio().getCftv().getUrl())){
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}


	public Boolean condominioPossuiAgrupamento(){
		if(condominioPossuiAgrupamento == null){
			condominioPossuiAgrupamento = condominioService.condominioPossuiAgrupamento(condominio.getId());
		}
		return condominioPossuiAgrupamento;
	}


	public Condominio getCondominio() {
		return condominio;
	}

	public Usuario getUsuario() {
		return usuario;
	}


}
