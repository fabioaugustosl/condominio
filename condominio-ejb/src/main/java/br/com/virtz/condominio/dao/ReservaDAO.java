package br.com.virtz.condominio.dao;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.virtz.condominio.entidades.AreaComum;
import br.com.virtz.condominio.entidades.Reserva;

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

	@Override
	public Reserva recuperar(AreaComum area, String nomeUsuario, Date dataInicioReserva) {
		
		Query qry = getEntityManager().createNamedQuery("Reserva.recuperarPorAreaNomeEData");
		qry.setParameter("idAreaComum", area.getId());
		qry.setParameter("nomeUsuario", nomeUsuario);
		qry.setParameter("data", dataInicioReserva);
		try {
			return (Reserva) qry.getSingleResult();
		}catch (NoResultException e) {
			return null;
		}
		
	}
	
	
}
