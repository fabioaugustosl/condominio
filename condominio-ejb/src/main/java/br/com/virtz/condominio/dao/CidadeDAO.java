package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.entidades.Cidade;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.Usuario;

@Stateless
public class CidadeDAO extends DAO<Cidade> implements ICidadeDAO {

	@Override
	public List<Cidade> recuperarCidadesComCondominiosCadastrados() {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT c.cidade ");
		sb.append(" FROM ").append(Condominio.class.getName()).append(" c ");
		sb.append(" GROUP BY c.cidade ");
		
		Query qry = getEntityManager().createQuery(sb.toString());
		
		return qry.getResultList();
	}

	
}
