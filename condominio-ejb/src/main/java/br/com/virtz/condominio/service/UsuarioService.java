package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.virtz.condominio.constantes.EnumTipoUsuario;
import br.com.virtz.condominio.dao.IUsuarioDAO;
import br.com.virtz.condominio.entidades.Condominio;
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
	public List<Usuario> recuperarTodos(Condominio condominio) {
		if(condominio == null){
			return null;
		}
		return usuarioDAO.recuperarTodos(condominio.getId());
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
	
	@Override
	public void alterarParaSindico(Long idUsuario){
		usuarioDAO.alterarStatus(idUsuario, EnumTipoUsuario.SINDICO);
	}

	@Override
	public void alterarParaMorador(Long idUsuario) {
		usuarioDAO.alterarStatus(idUsuario, EnumTipoUsuario.MORADOR);
	}

	@Override
	public void alterarParaAdministrativo(Long idUsuario) {
		usuarioDAO.alterarStatus(idUsuario, EnumTipoUsuario.ADMINISTRATIVO);
	}

}
