package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.constantes.EnumTipoPublicidade;
import br.com.virtz.condominio.entidades.Atalho;
import br.com.virtz.condominio.entidades.Publicidade;

@Stateless
public class PublicidadeDAO extends DAO<Publicidade> implements IPublicidadeDAO {

	
	@Override
	public Publicidade recuperar(Long idCondominio, EnumTipoPublicidade tipo) {
		if(idCondominio == null || tipo == null){
			return null;
		}
		
		Query qry = getEntityManager().createNamedQuery("Publicidade.recuperarPorCondominioTipo");
		qry.setParameter("idCondominio", idCondominio);
		qry.setParameter("tipo", tipo);
		qry.setMaxResults(300);
		
		
		List<Publicidade> lista = qry.getResultList();
		if(lista != null && !lista.isEmpty()){
			return lista.get(0);
		}
		return null;
	}

}
