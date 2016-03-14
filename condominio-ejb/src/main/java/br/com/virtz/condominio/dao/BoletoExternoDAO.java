package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.entidades.BoletoExterno;

@Stateless
public class BoletoExternoDAO extends DAO<BoletoExterno> implements IBoletoExternoDAO {

	@Override
	public BoletoExterno recuperar(Long idCondominio) {
		Query qry = getEntityManager().createNamedQuery("BoletoExterno.recuperarPorCondominio");
		qry.setParameter("idCondominio", idCondominio);
		List<BoletoExterno> regitros = qry.getResultList();
		
		if(regitros != null && !regitros.isEmpty()){
			return regitros.get(0);
		}
		return null;
	}
	
	
	
}
