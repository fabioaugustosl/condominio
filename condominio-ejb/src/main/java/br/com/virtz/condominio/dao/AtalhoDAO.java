package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.constantes.EnumFuncionalidadeAtalho;
import br.com.virtz.condominio.entidades.Atalho;
import br.com.virtz.condominio.entidades.MensagemSindico;

@Stateless
public class AtalhoDAO extends DAO<Atalho> implements IAtalhoDAO {

	@Override
	public List<Atalho> recuperar(Long idCondominio) {
		
		if(idCondominio == null){
			return null;
		}
		
		Query qry = getEntityManager().createNamedQuery("Atalho.recuperarPorCondominio");
		qry.setParameter("idCondominio", idCondominio);
		qry.setMaxResults(300);
		
		return qry.getResultList();
	}

	@Override
	public Atalho recuperar(Long idCondominio, EnumFuncionalidadeAtalho funcionalidade) {
		if(idCondominio == null || funcionalidade == null){
			return null;
		}
		
		Query qry = getEntityManager().createNamedQuery("Atalho.recuperarPorCondominioFuncionalidade");
		qry.setParameter("idCondominio", idCondominio);
		qry.setParameter("funcionalidade", funcionalidade);
		qry.setMaxResults(300);
		
		
		List<Atalho> lista = qry.getResultList();
		if(lista != null && !lista.isEmpty()){
			return lista.get(0);
		}
		return null;
	}

}
