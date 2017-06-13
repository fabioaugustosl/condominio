package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.entidades.Indicacao;

@Stateless
public class IndicacaoDAO extends DAO<Indicacao> implements IIndicacaoDAO {

	@Override
	public List<Indicacao> recuperar(Long idCondominio) {
		Query qry = getEntityManager().createNamedQuery("Indicacao.recuperarPorCondominio");
		qry.setParameter("idCondominio", idCondominio);
		return qry.getResultList();
	}

	@Override
	public List<Indicacao> recuperar(Long idCondominio, int limite) {
		Query qry = getEntityManager().createNamedQuery("Indicacao.recuperarPorCondominio");
		qry.setParameter("idCondominio", idCondominio);
		qry.setMaxResults(limite);
		return qry.getResultList();
	}
	
	@Override
	public List<Indicacao> recuperarPorCategoria(Long idCondominio, Long idCategoria) {
		StringBuilder sb = new StringBuilder();
		sb.append("Select n FROM indicacao n  ");
		sb.append(" JOIN n.categorias cat ");
		sb.append(" WHERE n.condominio.id = :idCondominio AND cat.id = :idCategoria  ");
		sb.append(" ORDER BY n.data desc ");
		
		Query qry = getEntityManager().createQuery(sb.toString());
		qry.setParameter("idCondominio", idCondominio);
		qry.setParameter("idCategoria", idCategoria);
		return qry.getResultList();
	}

}
