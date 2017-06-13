package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.entidades.Notificacao;

@Stateless
public class NotificacaoDAO extends DAO<Notificacao> implements INotificacaoDAO {

	@Override
	public List<Notificacao> recuperar(Long idCondominio, Long idUsuario) {
		Query qry = getEntityManager().createNamedQuery("Notificacao.recuperarPorCondominioEUsuario");
		qry.setParameter("idCondominio", idCondominio);
		qry.setParameter("idUsuario", idUsuario);
		return qry.getResultList();
	}
		
}
