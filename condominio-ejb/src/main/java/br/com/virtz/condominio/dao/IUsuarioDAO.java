package br.com.virtz.condominio.dao;

import javax.ejb.Local;

import br.com.virtz.condominio.entity.Usuario;

@Local
public interface IUsuarioDAO extends CrudDAO<Usuario> {
	
}
