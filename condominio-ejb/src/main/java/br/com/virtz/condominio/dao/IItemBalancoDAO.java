package br.com.virtz.condominio.dao;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.ItemBalanco;

@Local
public interface IItemBalancoDAO extends CrudDAO<ItemBalanco> {
	
}
