package br.com.virtz.condominio.dao;

import javax.ejb.Local;

import br.com.virtz.condominio.entity.Condominio;

@Local
public interface ICondominioDAO extends CrudDAO<Condominio> {
	
}
