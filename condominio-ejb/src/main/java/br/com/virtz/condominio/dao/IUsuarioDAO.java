package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.constantes.EnumTipoUsuario;
import br.com.virtz.condominio.entidades.Usuario;

@Local
public interface IUsuarioDAO extends CrudDAO<Usuario> {
	public Usuario recuperarUsuarioCompleto(Long id);
	public List<Usuario> recuperarTodos(Long idCondominio);
	public List<Usuario> recuperarSindicos(Long idCondominio);
	public List<Usuario> recuperar(Long idCondominio, EnumTipoUsuario tipo);
	public void alterarStatus(Long idUsuario, EnumTipoUsuario tipoUsuario);
	public Usuario recuperarUsuarioPorEmail(String email);
	public Usuario recuperarUsuarioPorEmailAutenticacao(String email);
	public List<Usuario> recuperarUsuariosPorEmail(String email);
	public List<Usuario> recuperarUsuariosPorApartamento(Long idApartamento);
	public List<Usuario> recuperarTodosAdm(Long idCondominio);
}
