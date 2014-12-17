package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.entidades.Avaliacao;
import br.com.virtz.condominio.entidades.BatePapo;
import br.com.virtz.condominio.entidades.Usuario;

@Stateless
public class AvaliacaoBatePapoDAO extends DAO<Avaliacao> implements IAvaliacaoBatePapoDAO {

	@Override
	public Avaliacao recuperar(BatePapo batePapo, Usuario usuario) {
		Query qry = getEntityManager().createNamedQuery("Avaliacao.recuperarAvalicaoPorBatepapoEUsuario");
		qry.setParameter("idUsuario", usuario.getId());
		qry.setParameter("idBatePapo", batePapo.getId());
		
		List<Avaliacao> avaliacoes = qry.getResultList();
		return avaliacoes == null || avaliacoes.isEmpty() ? null : avaliacoes.get(0);
	}
	
	@Override
	public Avaliacao salvar(Avaliacao t) throws Exception {
		return super.salvar(t);
	}
	
}
