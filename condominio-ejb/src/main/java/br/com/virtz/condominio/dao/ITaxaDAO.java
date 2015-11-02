package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.Taxa;

@Local
public interface ITaxaDAO extends CrudDAO<Taxa> {
	public List<Taxa> recuperarPorCondominio(Long idCondominio);
}
