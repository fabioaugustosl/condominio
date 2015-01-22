package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.Noticia;

@Stateless
public class NoticiaDAO extends DAO<Noticia> implements INoticiaDAO {

	@Override
	public List<Noticia> recuperar(Condominio condominio) {
		Query qry = getEntityManager().createNamedQuery("Noticia.recuperarPorCondominio");
		qry.setParameter("idCondominio", condominio);
		return qry.getResultList();
	}
	
}
