package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.virtz.condominio.entidades.Bloco;

@Stateless
public class BlocoDAO extends DAO<Bloco> implements IBlocoDAO {

	@Override
	public List<Bloco> recuperarComApartamentos(Long idCondominio) {
		Query qry = getEntityManager().createNamedQuery("Bloco.recuperarPorCondominioComApts");
		qry.setParameter("idCondominio", idCondominio);
		return qry.getResultList();
	}
	
	@Override
	public Bloco recuperarBlocoCompleto(Long idBloco) {
		Query qry = getEntityManager().createNamedQuery("Bloco.recuperarBlocoCompleto");
		qry.setParameter("idBloco", idBloco);
		try{
			return (Bloco) qry.getSingleResult();
		}catch(NoResultException no){
			return null;
		}
	}
	
	
}
