package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.entidades.RespostaMensagemSindico;

@Stateless
public class RespostaMensagemSindicoDAO extends DAO<RespostaMensagemSindico> implements IRespostaMensagemSindicoDAO {

	@Override
	public List<RespostaMensagemSindico> recuperar(Long idMensagem) {
		if(idMensagem == null){
			return null;
		}
		
		Query qry = getEntityManager().createNamedQuery("RespostaMensagemSindico.recuperarPorMensagem");
		qry.setParameter("idMensagem", idMensagem);
		
		return qry.getResultList();
	}


}
