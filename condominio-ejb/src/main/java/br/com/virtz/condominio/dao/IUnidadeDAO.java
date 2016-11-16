package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.Unidade;

@Local
public interface IUnidadeDAO extends CrudDAO<Unidade> {

	public List<Unidade> recuperarPorCondominio(Long idCondominio);

}
