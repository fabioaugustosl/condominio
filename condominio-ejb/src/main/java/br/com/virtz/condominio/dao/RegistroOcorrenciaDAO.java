package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.entidades.RegistroOcorrencia;

@Stateless
public class RegistroOcorrenciaDAO extends DAO<RegistroOcorrencia> implements IRegistroOcorrenciaDAO {

	@Override
	public List<RegistroOcorrencia> recuperar(Long idCondominio) {
		if(idCondominio == null){
			return null;
		}

		Query qry = getEntityManager().createNamedQuery("RegistroOcorrencia.recuperarPorCondominio");
		qry.setParameter("idCondominio", idCondominio);

		return qry.getResultList();
	}


	@Override
	public List<RegistroOcorrencia> recuperarPaginado(Long idCondominio, int inicio, int qtdRegistros) {
		Query qry = getEntityManager().createNamedQuery("RegistroOcorrencia.recuperarPorCondominio");
		qry.setParameter("idCondominio", idCondominio);
		qry.setFirstResult(inicio);
		qry.setMaxResults(qtdRegistros);
		return qry.getResultList();
	}


	@Override
	public int totalOcorrencias(Long idCondominio) {
		Query qry = getEntityManager().createNamedQuery("RegistroOcorrencia.totalOcorrencias");
		qry.setParameter("idCondominio", idCondominio);
		Long total =  (Long) qry.getSingleResult();
		if(total > 1000l){
			total = 1000l;
		}
		return total.intValue();
	}


}
