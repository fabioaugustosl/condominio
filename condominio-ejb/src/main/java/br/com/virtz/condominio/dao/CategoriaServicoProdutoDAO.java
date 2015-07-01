package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.entidades.CategoriaServicoProduto;

@Stateless
public class CategoriaServicoProdutoDAO extends DAO<CategoriaServicoProduto> implements ICategoriaServicoProdutoDAO {

	@Override
	public List<CategoriaServicoProduto> recuperarComQuantidade() {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT new ").append(CategoriaServicoProduto.class.getName()).append("( ");
		sb.append("c.id, c.nome, (select count(i) from CategoriaServicoProduto c1 JOIN c1.indicacoes i WHERE c1.id = c.id) )");
		sb.append(" FROM ").append(CategoriaServicoProduto.class.getName()).append(" c");
		
		Query qry = getEntityManager().createQuery(sb.toString());
		
		return qry.getResultList();
	}

	
}
