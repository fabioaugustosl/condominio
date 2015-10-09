package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.entidades.Recebido;

@Stateless
public class RecebidoDAO extends DAO<Recebido> implements IRecebidoDAO {

	@Override
	public List<Recebido> recuperarPorApartamento(Long idApartamento) {
		Query qry = getEntityManager().createNamedQuery("Recebido.recuperarPorApto");
		qry.setParameter("idApartamento", idApartamento);
		return qry.getResultList();
	}

	@Override
	public List<Recebido> recuperarPorCondominio(Long idCondominio) {
		Query qry = getEntityManager().createNamedQuery("Recebido.recuperarPorCondominio");
		qry.setParameter("idCondominio", idCondominio);
		return qry.getResultList();
	}
		
}
