package br.com.virtz.condominio.dao;

import javax.ejb.Local;

import br.com.virtz.condominio.entity.Bloco;

@Local
public interface IBlocoDAO extends CrudDAO<Bloco> {
	
}
