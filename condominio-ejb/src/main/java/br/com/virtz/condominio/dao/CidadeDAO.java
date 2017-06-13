package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.entidades.Cidade;
import br.com.virtz.condominio.entidades.Condominio;

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

	@Override
	public List<Cidade> recuperarPorEstado(Long idEstado) {
		Query qry = getEntityManager().createNamedQuery("Cidade.recuperarPorEstado");
		qry.setParameter("idEstado", idEstado);
		return qry.getResultList();
	}

	
}
