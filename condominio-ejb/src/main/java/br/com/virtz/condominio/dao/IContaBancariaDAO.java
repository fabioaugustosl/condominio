package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.ContaBancariaCondominio;

@Local
public interface IContaBancariaDAO extends CrudDAO<ContaBancariaCondominio> {
	public List<ContaBancariaCondominio> recuperarPorCondominio(Long idCondominio);
}
