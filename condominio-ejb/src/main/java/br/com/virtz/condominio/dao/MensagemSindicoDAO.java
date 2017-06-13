package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.entidades.MensagemSindico;

@Stateless
public class MensagemSindicoDAO extends DAO<MensagemSindico> implements IMensagemSindicoDAO {

	@Override
	public List<MensagemSindico> recuperar(Long idCondominio) {
		
		if(idCondominio == null){
			return null;
		}
		
		Query qry = getEntityManager().createNamedQuery("MensagemSindico.recuperarPorCondominio");
		qry.setParameter("idCondominio", idCondominio);
		qry.setMaxResults(300);
		
		return qry.getResultList();
	}

}
