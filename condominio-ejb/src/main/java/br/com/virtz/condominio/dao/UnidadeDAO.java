package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.entidades.Unidade;

@Stateless
public class UnidadeDAO extends DAO<Unidade> implements IUnidadeDAO {


	@Override
	public List<Unidade> recuperarPorCondominio(Long idCondominio) {
		Query qry = getEntityManager().createNamedQuery("Unidade.recuperarPorCondominio");
		qry.setParameter("idCondominio", idCondominio);
		return qry.getResultList();
	}

}
