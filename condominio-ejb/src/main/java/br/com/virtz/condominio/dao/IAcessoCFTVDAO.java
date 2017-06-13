package br.com.virtz.condominio.dao;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.AcessoCFTV;

@Local
public interface IAcessoCFTVDAO extends CrudDAO<AcessoCFTV> {
	
	public AcessoCFTV recuperar(Long idCondominio);
	
}
