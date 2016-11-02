package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.entidades.AgrupamentoUnidades;

@Stateless
public class AgrupamentoUnidadesDAO extends DAO<AgrupamentoUnidades> implements IAgrupamentoUnidadesDAO {

	@Override
	public List<AgrupamentoUnidades> recuperarComBlocos(Long idCondominio) {
		Query qry = getEntityManager().createNamedQuery("AgrupamentoUnidades.recuperarPorCondominioComBlocos");
		qry.setParameter("idCondominio", idCondominio);
		return qry.getResultList();
	}


}
