package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.Balanco;

@Local
public interface IBalancoDAO extends CrudDAO<Balanco> {
	
	public List<Balanco> recuperarPorCondominio(Long idCondominio);
	public Balanco recuperarPorCondominio(Long idCondominio, Integer ano, Integer mes);
	
}
