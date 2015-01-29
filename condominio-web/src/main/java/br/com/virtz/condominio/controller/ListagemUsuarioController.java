package br.com.virtz.condominio.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exceptions.CondominioException;
import br.com.virtz.condominio.service.IUsuarioService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.IArquivosUtil;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class ListagemUsuarioController {

	@EJB
	private IUsuarioService usuarioService;
	
	@Inject
	private MessageHelper messageHelper;
	
	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private NavigationPage navegacao;
	
	@Inject
	private IArquivosUtil arquivoUtil;
	
	private List<Usuario> usuarios;
	
	
	
	@PostConstruct
	public void init(){
		usuarios = listarTodos(); 
	}
	
	
	public List<Usuario> listarTodos(){
		Usuario usuario = sessao.getUsuarioLogado();
		
		List<Usuario> usuarios = usuarioService.recuperarTodos(usuario.getCondominio());
		
		return usuarios;
	}

	
	public void remover(Usuario usuario) throws CondominioException {
		usuarioService.remover(usuario.getId());
		messageHelper.addInfo("Usuário removido com sucesso!");
	}
	
	
	public void alterarParaSindico(Usuario usuario) throws CondominioException {
		if(usuario != null){
			usuarioService.alterarParaSindico(usuario.getId());
			messageHelper.addInfo("Usuário alterado para Síndico!");
		}
	}
	
	public void alterarParaAdministrativo(Usuario usuario) throws CondominioException {
		if(usuario != null){
			usuarioService.alterarParaAdministrativo(usuario.getId());
			messageHelper.addInfo("Usuário alterado para Administrativo!");
		}
	}
	
	public void alterarParaMorador(Usuario usuario) throws CondominioException {
		if(usuario != null){
			usuarioService.alterarParaMorador(usuario.getId());
			messageHelper.addInfo("Usuário alterado para Morador!");
		}
	}
	
	


	
	// GETTERS E SETTERS
	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

}
