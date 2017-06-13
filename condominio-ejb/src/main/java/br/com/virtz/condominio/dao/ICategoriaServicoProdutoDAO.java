package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.CategoriaServicoProduto;

@Local
public interface ICategoriaServicoProdutoDAO extends CrudDAO<CategoriaServicoProduto> {
	public List<CategoriaServicoProduto> recuperarComQuantidade();
	public List<CategoriaServicoProduto> recuperarComQuantidade(Long idCondominio); 
}
