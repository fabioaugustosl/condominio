package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.entidades.Bloco;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.Votacao;

@Stateless
public class VotacaoDAO extends DAO<Votacao> implements IVotacaoDAO {

	@Override
	public List<Votacao> recuperar(Condominio condominio) {
		Query qry = getEntityManager().createNamedQuery("Votacao.recuperarPorCondominio");
		qry.setParameter("idCondominio", condominio.getId());
		return qry.getResultList();
	}
	
}
