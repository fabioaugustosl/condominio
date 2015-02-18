package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.constantes.EnumTipoUsuario;
import br.com.virtz.condominio.entidades.Usuario;

@Local
public interface IUsuarioDAO extends CrudDAO<Usuario> {
	public Usuario recuperarUsuarioCompleto(Long id);
	public List<Usuario> recuperarTodos(Long idCondominio);
	public void alterarStatus(Long idUsuario, EnumTipoUsuario tipoUsuario);
	public Usuario recuperarUsuarioPorEmail(String email); 
}
