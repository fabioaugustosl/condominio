package br.com.virtz.condominio.dao;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.Usuario;

@Local
public interface ICondominioDAO extends CrudDAO<Condominio> {
	public Condominio recuperarCondominioCompleto(Usuario usuario);
}
