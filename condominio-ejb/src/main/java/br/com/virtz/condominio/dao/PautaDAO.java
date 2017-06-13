package br.com.virtz.condominio.dao;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.entidades.PautaAssembleia;

@Stateless
public class PautaDAO extends DAO<PautaAssembleia> implements IPautaDAO {
	
	@Override
	public void remover(Long id) {
		StringBuilder sb = new StringBuilder();
		sb.append("delete ").append(PautaAssembleia.class.getName()).append(" WHERE id = :id ");
		Query qry = getEntityManager().createQuery(sb.toString());
		qry.setParameter("id", id);
		qry.executeUpdate();
	}
}
