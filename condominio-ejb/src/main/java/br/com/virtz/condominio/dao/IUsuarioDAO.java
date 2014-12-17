package br.com.virtz.condominio.dao;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.Usuario;

@Local
public interface IUsuarioDAO extends CrudDAO<Usuario> {
	public Usuario recuperarUsuarioCompleto(Long id);
}
