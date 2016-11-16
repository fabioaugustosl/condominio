package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.entidades.NotificacaoPortaria;

@Stateless
public class NotificacaoPortariaDAO extends DAO<NotificacaoPortaria> implements INotificacaoPortariaDAO {

	@Override
	public List<NotificacaoPortaria> recuperarPorCondominio(Long idCondominio) {
		Query qry = getEntityManager().createNamedQuery("NotificacaoPortaria.recuperarPorCondominio");
		qry.setParameter("idCondominio", idCondominio);
		return qry.getResultList();
	}

}
