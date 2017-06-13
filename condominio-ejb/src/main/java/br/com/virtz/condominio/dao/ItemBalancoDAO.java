package br.com.virtz.condominio.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.constantes.EnumTipoBalanco;
import br.com.virtz.condominio.entidades.ItemBalanco;

@Stateless
public class ItemBalancoDAO extends DAO<ItemBalanco> implements IItemBalancoDAO {

	@Override
	public List<String> recuperarUltimasDescricoes(Long idCondominio, Integer ano, EnumTipoBalanco tipo, Integer limite) {
		Query qry = getEntityManager().createNamedQuery("ItemBalanco.recuperarUltimasDescricoes");
		qry.setParameter("idCondominio", idCondominio);
		qry.setParameter("ano", ano);
		qry.setParameter("tipo", tipo);
		qry.setMaxResults(limite.intValue());
		List<Object[]> objs = qry.getResultList();
		List<String> resultados = new ArrayList<String>();
		if(objs != null && !objs.isEmpty()){
			for(Object o : objs){
				resultados.add(o.toString());
			}
		}
		return resultados;
	}

		
}
