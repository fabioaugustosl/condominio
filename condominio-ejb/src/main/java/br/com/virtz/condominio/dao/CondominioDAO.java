package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.Usuario;

@Stateless
public class CondominioDAO extends DAO<Condominio> implements ICondominioDAO {

	@Override
	public Condominio recuperarCondominioCompleto(Usuario usuario) {
		
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT c ");
		sb.append(" FROM ").append(Condominio.class.getName()).append(" c ");
		sb.append(" JOIN FETCH c.bloco b ");
		sb.append(" JOIN FETCH c.areasComuns areas ");
		sb.append(" JOIN c.usuarios u ");
		sb.append(" WHERE u.id = :idUsuario ");
		
		Query query = getEntityManager().createQuery("SELECT c");
		
		query.setParameter("idUsuario", usuario.getId());
		
		List<Condominio> condominios = query.getResultList();
		
		return (condominios != null && condominios.size() > 0) ? condominios.get(0) : null;
	}

	
	@Override
	public List<Condominio> recuperarPorCidade(Long idCidade) {
		Query qry = getEntityManager().createNamedQuery("Condominio.recuperarPorCidade");
		qry.setParameter("idCidade", idCidade);
		
		return qry.getResultList();
	}
	
}
