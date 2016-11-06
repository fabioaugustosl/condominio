package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.constantes.EnumFuncaoBloqueio;
import br.com.virtz.condominio.entidades.BloqueioFuncaoUsuario;

@Stateless
public class BloqueioFuncaoUsuarioDAO extends DAO<BloqueioFuncaoUsuario> implements IBloqueioFuncaoUsuarioDAO {


	@Override
	public List<BloqueioFuncaoUsuario> recuperarPorUsuarioEFuncao(Long idUsuario, EnumFuncaoBloqueio funcao) {
		Query qry = getEntityManager().createNamedQuery("BloqueioFuncaoUsuario.recuperarPorFuncaoUsuario");
		qry.setParameter("idUsuario", idUsuario);
		qry.setParameter("funcaoBloqueio", funcao);

		return qry.getResultList();
	}


	@Override
	public List<BloqueioFuncaoUsuario> recuperarPorCondominioEFuncao(Long idCondominio, EnumFuncaoBloqueio funcao) {
		Query qry = getEntityManager().createNamedQuery("BloqueioFuncaoUsuario.recuperarPorFuncaoECondominio");
		qry.setParameter("idCondominio", idCondominio);
		qry.setParameter("funcaoBloqueio", funcao);

		return qry.getResultList();
	}


	@Override
	public List<BloqueioFuncaoUsuario> recuperarPorUsuario(Long idUsuario) {
		Query qry = getEntityManager().createNamedQuery("BloqueioFuncaoUsuario.recuperarPorUsuario");
		qry.setParameter("idUsuario", idUsuario);

		return qry.getResultList();
	}

}
