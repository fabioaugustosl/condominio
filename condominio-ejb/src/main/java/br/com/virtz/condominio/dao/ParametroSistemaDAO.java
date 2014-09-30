package br.com.virtz.condominio.dao;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.constantes.EnumParametroSistema;
import br.com.virtz.condominio.entity.ParametroSistema;

@Stateless
public class ParametroSistemaDAO extends DAO<ParametroSistema> implements IParametroSistemaDAO {

	@Override
	public ParametroSistema recuperar(EnumParametroSistema parametro) {
		Query qry = getEntityManager().createQuery("FROM "+ParametroSistema.class.getName()+" p WHERE p.id = :idParametro ");
		qry.setParameter("idParametro", parametro.getId());
		return (ParametroSistema) qry.getSingleResult();
	}
	
	
}
