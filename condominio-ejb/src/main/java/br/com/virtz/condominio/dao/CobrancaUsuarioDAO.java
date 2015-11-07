package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.entidades.CobrancaUsuario;

@Stateless
public class CobrancaUsuarioDAO extends DAO<CobrancaUsuario> implements ICobrancaUsuarioDAO {


	@Override
	public List<CobrancaUsuario> recuperarPorCondominio(Long idCondominio, Integer ano, Integer mes) {
		Query qry = getEntityManager().createNamedQuery("CobrancaUsuario.recuperarPorCondominioAnoMes");
		qry.setParameter("idCondominio", idCondominio);
		qry.setParameter("ano", ano);
		qry.setParameter("mes", mes);
		
		return qry.getResultList();
	}
	
	
	@Override
	public List<CobrancaUsuario> recuperarCobrancasDeAvulsosPorCondominio(Long idCondominio, Integer ano, Integer mes) {
		Query qry = getEntityManager().createNamedQuery("CobrancaUsuario.recuperarPorCondominioAnoMesTodosAvulsos");
		qry.setParameter("idCondominio", idCondominio);
		qry.setParameter("ano", ano);
		qry.setParameter("mes", mes);
		
		return qry.getResultList();
	}


	@Override
	public CobrancaUsuario recuperar(Long idCondominio, Long idUsuario,
			Integer ano, Integer mes) {
		Query qry = getEntityManager().createNamedQuery("CobrancaUsuario.recuperarPorCondominioUsuarioAnoMes");
		qry.setParameter("idCondominio", idCondominio);
		qry.setParameter("idUsuario", idUsuario);
		qry.setParameter("ano", ano);
		qry.setParameter("mes", mes);
		
		List<CobrancaUsuario> resultado = qry.getResultList();
		if(resultado == null || resultado.isEmpty()){
			return null;
		}
		return resultado.get(0);
	}


	@Override
	public List<CobrancaUsuario> recuperarPorCondominio(Long idCondominio) {
		Query qry = getEntityManager().createNamedQuery("CobrancaUsuario.recuperarPorCondominio");
		qry.setParameter("idCondominio", idCondominio);
		
		return qry.getResultList();
	}
	
	
	@Override
	public List<Object[]> recuperarAnosMeses(Long idCondominio) {
		Query qry = getEntityManager().createNamedQuery("CobrancaUsuario.recuperarAnosMesesPorCondominio");
		qry.setParameter("idCondominio", idCondominio);
		
		return qry.getResultList();
	}

	
}
