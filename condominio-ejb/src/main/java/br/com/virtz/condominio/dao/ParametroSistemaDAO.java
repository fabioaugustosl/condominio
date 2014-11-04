package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.constantes.EnumParametroSistema;
import br.com.virtz.condominio.entity.Condominio;
import br.com.virtz.condominio.entity.ParametroSistema;

@Stateless
public class ParametroSistemaDAO extends DAO<ParametroSistema> implements IParametroSistemaDAO {

	@Override
	public ParametroSistema recuperar(EnumParametroSistema parametro, Condominio condominio) {
		Query qry = getEntityManager().createQuery("FROM "+ParametroSistema.class.getName()+" p WHERE p.id = :idParametro and p.condominio.id = :idCondominio ");
		qry.setParameter("idParametro", parametro.getId());
		qry.setParameter("idCondominio", condominio.getId());
		return (ParametroSistema) qry.getSingleResult();
	}

	@Override
	public List<ParametroSistema> recuperarTodos(Condominio condominio) {
		Query qry = getEntityManager().createQuery("FROM "+ParametroSistema.class.getName()+" p WHERE p.condominio.id = :idCondominio ");
		qry.setParameter("idCondominio", condominio.getId());
		return qry.getResultList();
	}
	
	
}
