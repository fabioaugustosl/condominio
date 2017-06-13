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
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class ListagemAdministradorController {

	@EJB
	private IUsuarioService usuarioService;

	@Inject
	private MessageHelper messageHelper;

	@Inject
	private SessaoUsuario sessao;

	@Inject
	private NavigationPage navegacao;


	private List<Usuario> usuarios;
	private Usuario usuarioSelecionado = null;

	Usuario usuario = null;

	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
		usuarios = listarTodos();
	}


	public List<Usuario> listarTodos(){
		return usuarioService.recuperarTodosAdministradores(usuario.getCondominio().getId());
	}


	public void remover(Usuario usuario) throws CondominioException {
		usuarioService.remover(usuario.getId());
		usuarios.remove(usuario);
		messageHelper.addInfo("Administrador removido com sucesso!");
	}


	@SuppressWarnings("static-access")
	public void irParaCadastro(){
		navegacao.redirectToPage("/usuario/cadastrarUsuarioAdministrador.faces");
	}


	// GETTERS E SETTERS
	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Usuario getUsuarioSelecionado() {
		return usuarioSelecionado;
	}

	public void setUsuarioSelecionado(Usuario usuarioSelecionado) {
		this.usuarioSelecionado = usuarioSelecionado;
	}


}
