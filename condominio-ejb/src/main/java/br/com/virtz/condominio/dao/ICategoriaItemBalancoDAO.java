package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.CategoriaItemBalanco;

@Local
public interface ICategoriaItemBalancoDAO extends CrudDAO<CategoriaItemBalanco> {
	public List<CategoriaItemBalanco> recuperarPorCondominio(Long idCondominio);
	public CategoriaItemBalanco recuperarPorCondominio(Long idCondominio, String nome);
}
