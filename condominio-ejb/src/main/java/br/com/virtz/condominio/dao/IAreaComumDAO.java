package br.com.virtz.condominio.dao;

import javax.ejb.Local;

import br.com.virtz.condominio.entity.AreaComum;

@Local
public interface IAreaComumDAO extends CrudDAO<AreaComum> {
	
}
