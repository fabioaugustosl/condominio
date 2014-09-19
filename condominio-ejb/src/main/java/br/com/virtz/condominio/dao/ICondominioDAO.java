package br.com.virtz.condominio.dao;

import javax.ejb.Local;

import br.com.virtz.condominio.entity.Condominio;
import br.com.virtz.condominio.entity.Usuario;

@Local
public interface ICondominioDAO extends CrudDAO<Condominio> {
	public Condominio recuperarCondominioCompleto(Usuario usuario);
}
