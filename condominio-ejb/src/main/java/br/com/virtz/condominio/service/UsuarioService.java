package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.virtz.condominio.dao.IUsuarioDAO;
import br.com.virtz.condominio.entidades.Usuario;

@Stateless
public class UsuarioService implements IUsuarioService {

	@EJB
	private IUsuarioDAO usuarioDAO;

	@Override
	public List<Usuario> recuperarTodos() {
		return usuarioDAO.recuperarTodos();
	}

	@Override
	public void remover(Long id) {
		usuarioDAO.remover(id);
	}

	@Override
	public Usuario salvar(Usuario usuario) throws Exception {
		return usuarioDAO.salvar(usuario);
	}

	@Override
	public Usuario recuperarUsuarioCompleto(Long id) {
		return usuarioDAO.recuperarUsuarioCompleto(id);
	}

}
