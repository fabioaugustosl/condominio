package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;
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
	
}
