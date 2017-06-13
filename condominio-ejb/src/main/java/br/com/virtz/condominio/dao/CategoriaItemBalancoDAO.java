package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.constantes.EnumTipoBalanco;
import br.com.virtz.condominio.entidades.CategoriaItemBalanco;

@Stateless
public class CategoriaItemBalancoDAO extends DAO<CategoriaItemBalanco> implements ICategoriaItemBalancoDAO {

	@Override
	public List<CategoriaItemBalanco> recuperarPorCondominio(Long idCondominio) {
		Query qry = getEntityManager().createNamedQuery("CategoriaItemBalanco.recuperarCategoriaItemBalanco");
		qry.setParameter("idCondominio", idCondominio);
		return qry.getResultList();
	}

	
	@Override
	public CategoriaItemBalanco recuperarPorCondominio(Long idCondominio, String nome) {
		
		Query qry = getEntityManager().createNamedQuery("CategoriaItemBalanco.recuperarCategoriaPorNomeECondominio");
		qry.setParameter("idCondominio", idCondominio);
		qry.setParameter("nome", nome);
		List<CategoriaItemBalanco> lista = qry.getResultList();
		if(lista != null && !lista.isEmpty()){
			return lista.get(0);
		}
		return null;
	}

	
	@Override
	public List<CategoriaItemBalanco> recuperarPorCondominio(Long idCondominio, EnumTipoBalanco tipoBalanco) {
		Query qry = getEntityManager().createNamedQuery("CategoriaItemBalanco.recuperarCategoriaPorTipo");
		qry.setParameter("idCondominio", idCondominio);
		qry.setParameter("tipo", tipoBalanco);
		return qry.getResultList();
	}

}
