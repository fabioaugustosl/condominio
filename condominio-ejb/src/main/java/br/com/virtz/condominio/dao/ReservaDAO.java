package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.entity.AreaComum;
import br.com.virtz.condominio.entity.Reserva;

@Stateless
public class ReservaDAO extends DAO<Reserva> implements IReservaDAO {

	@Override
	public List<Reserva> recuperar(AreaComum area) {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT r FROM ");
		sb.append(Reserva.class.getName()).append(" r ");
		sb.append(" WHERE r.areaComum.id = :idArea  ");
		
		Query qry = getEntityManager().createQuery(sb.toString());
		
		qry.setParameter("idArea", area.getId());
		
		return qry.getResultList();
	}
	
	
}
