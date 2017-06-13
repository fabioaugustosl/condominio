package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.entidades.Noticia;

@Stateless
public class NoticiaDAO extends DAO<Noticia> implements INoticiaDAO {

	@Override
	public List<Noticia> recuperar(Long idCondominio) {
		Query qry = getEntityManager().createNamedQuery("Noticia.recuperarPorCondominio");
		qry.setParameter("idCondominio", idCondominio);
		return qry.getResultList();
	}

	@Override
	public List<Noticia> recuperarAtivas(Long idCondominio, Integer totalRegistros) {
		Query qry = getEntityManager().createNamedQuery("Noticia.recuperarPorCondominioAtivas");
		qry.setParameter("idCondominio", idCondominio);
		if(totalRegistros!=null && totalRegistros > 0){
			qry.setMaxResults(totalRegistros);
		}
		return qry.getResultList();
	}


}
