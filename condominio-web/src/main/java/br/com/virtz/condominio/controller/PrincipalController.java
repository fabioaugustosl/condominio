package br.com.virtz.condominio.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;

import br.com.virtz.condominio.constantes.EnumTipoUsuario;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.session.SessaoUsuario;

@ManagedBean
@SessionScoped
public class PrincipalController implements Serializable {

	private static final long serialVersionUID = 1L;


	@Inject
	private SessaoUsuario sessao;
	
	private Condominio condominio = null;
	private Usuario usuario = null;
	
	
	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
		condominio = usuario.getCondominio();
	}
		 
	 
	public boolean ehSindico(){
		 if(EnumTipoUsuario.SINDICO.equals(sessao.getUsuarioLogado().getTipoUsuario())){
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

	
	
	public Condominio getCondominio() {
		return condominio;
	}

	public Usuario getUsuario() {
		return usuario;
	}
	
				
}
